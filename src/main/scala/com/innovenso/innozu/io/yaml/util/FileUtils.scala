package com.innovenso.innozu.io.yaml.util

import fish.genius.logging.Loggable

import java.io.{
  File,
  FileInputStream,
  FileOutputStream,
  InputStream,
  OutputStream,
  OutputStreamWriter,
  PrintWriter
}
import java.net.URL
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Path
import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}

object FileUtils extends Loggable {
  def readFileToString(
                        file: File,
                        charset: Charset = StandardCharsets.UTF_8
                      ): Option[String] = {
    val output = withBufferedSource(file) { bufferedSource =>
      val lines = bufferedSource.getLines().toList
      lines.mkString("\n")
    }
    debug(s"read from file ${file.getAbsolutePath}: $output")
    output
  }
  def readPathToString(
                        path: Path,
                        charset: Charset = StandardCharsets.UTF_8
                      ): Option[String] = readFileToString(path.toFile, charset)

  def writeStringToFile(
                         input: String,
                         targetFile: File,
                         charset: Charset = StandardCharsets.UTF_8
                       ): Option[File] =
    withOutputStreamWriter(prepareTargetFile(targetFile), charset) {
      outputStreamWriter =>
        outputStreamWriter.write(input)
        debug(s"wrote to file ${targetFile.getAbsolutePath}: $input")
        targetFile
    }

  val temporaryDirectory: File = new File(
    sys.props.getOrElse("java.io.tmpdir", "/tmp")
  )

  val userDirectory: File = new File(sys.props.getOrElse("user.home", "/tmp"))

  private def prepareTargetFile(targetFile: File): File = {
    Option(targetFile.getParentFile).foreach(p => p.mkdirs())
    targetFile
  }

  private val COPY_BUFFER_SIZE = 8192
  def copyResourceFromFileOrClassPathToFile(
                                             path: String,
                                             targetFile: File,
                                             clazz: Class[_] = this.getClass
                                           ): Option[File] = withInputStream(path, clazz) { inputStream =>
    withFileOutputStream(prepareTargetFile(targetFile)) { fileOutputStream =>
      copy(inputStream, fileOutputStream)
      targetFile
    }
  }.flatten

  def copy(source: File, target: File): Option[File] = Try {
    withInputStream(new FileInputStream(source)) { inputStream =>
      withFileOutputStream(prepareTargetFile(target)) { outputStream =>
        copy(inputStream, outputStream)
        target
      }
    }.flatten
  }.getOrElse(None)

  def copyUrlToFile(url: String, targetFile: File): Option[File] = Try {
    val theUrl = new URL(url)
    val urlConnection = theUrl.openConnection
    val inputStream = urlConnection.getInputStream
    withInputStream(inputStream) { inputStream =>
      withFileOutputStream(prepareTargetFile(targetFile)) { fileOutputStream =>
        copy(inputStream, fileOutputStream)
        targetFile
      }
    }.flatten
  }.getOrElse(None)

  private def copy(
                    inputStream: InputStream,
                    outputStream: OutputStream
                  ): Unit = {
    val buffer = new Array[Byte](COPY_BUFFER_SIZE)
    Iterator continually (inputStream read buffer) takeWhile (_ != -1) filter (_ > 0) foreach {
      read =>
        outputStream.write(buffer, 0, read)
        outputStream.flush()
    }

  }

  private def withBufferedSource[O](
                                     file: File
                                   )(operation: BufferedSource => O): Option[O] = Try {
    val bufferedSource: BufferedSource = Source.fromFile(file)
    withBufferedSource(bufferedSource)(operation)
  }.getOrElse(None)

  private def withInputStream[O](
                                  path: String,
                                  clazz: Class[_] = this.getClass
                                )(operation: InputStream => O): Option[O] = Try {
    debug(s"get InputStream for path $path")
    val maybeFile: File = new File(s".$path")
    debug(s"first trying ${maybeFile.getAbsolutePath}")
    if (maybeFile.exists() && maybeFile.canRead) {
      debug("file exists on the filesystem, using that")
      withInputStream(new FileInputStream(maybeFile))(operation)
    } else {
      debug("file does not exist on the filesystem, checking classpath")
      val resourcePath: Option[URL] =
        Option(clazz.getResource(path))
      if (resourcePath.isEmpty) {
        debug("resource not found on classpath")
        None
      } else {
        withInputStream(clazz.getResourceAsStream(path))(
          operation
        )
      }
    }
  }.getOrElse(None)

  private def withInputStream[O](
                                  inputStream: InputStream
                                )(operation: InputStream => O): Option[O] = {
    val output: Option[O] = Try {
      operation.apply(inputStream)
    } match {
      case Success(value) => Some(value)
      case Failure(cause) => {
        exception(cause)
        None
      }
    }
    try {
      inputStream.close()
    } catch {
      case t: Throwable => warning(t.getMessage)
    }
    output
  }

  private def withBufferedSource[O](
                                     bufferedSource: BufferedSource
                                   )(operation: BufferedSource => O): Option[O] = {
    val output: Option[O] = Try {
      operation.apply(bufferedSource)
    } match {
      case Success(value) => Some(value)
      case Failure(cause) => {
        exception(cause)
        None
      }
    }
    bufferedSource.close()
    output
  }

  private def withOutputStreamWriter(
                                      targetFile: File,
                                      charset: Charset
                                    )(operation: OutputStreamWriter => File): Option[File] = Try {
    val outputStreamWriter =
      new OutputStreamWriter(new FileOutputStream(targetFile), charset)

    val output: Option[File] = Try {
      operation.apply(outputStreamWriter)
    } match {
      case Success(value) => Some(value)
      case Failure(cause) => {
        exception(cause)
        None
      }
    }
    try {
      outputStreamWriter.close()
    } catch {
      case t: Throwable => warning(t.getMessage)
    }
    output
  }.getOrElse(None)

  private def withFileOutputStream(
                                    targetFile: File
                                  )(operation: FileOutputStream => File): Option[File] = Try {
    val fileOutputStream = new FileOutputStream(targetFile)

    val output: Option[File] = Try {
      operation.apply(fileOutputStream)
    } match {
      case Success(value) => Some(value)
      case Failure(cause) => {
        exception(cause)
        None
      }
    }
    try {
      fileOutputStream.close()
    } catch {
      case t: Throwable => warning(t.getMessage)
    }
    output
  }.getOrElse(None)
}
