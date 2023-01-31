package com.innovenso.innozu.io.yaml

import org.yaml.snakeyaml.{DumperOptions, Yaml}

trait HasSnakeYaml {
  val options = new DumperOptions()
  options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
  val yaml: Yaml = new Yaml(options)

}
