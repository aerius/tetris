locals {
  basicinfra_version                       = "v1"

  ecr_repo                                 = "028339422996.dkr.ecr.eu-west-1.amazonaws.com"

  app_name                                 = "aerius"
  app_timezone                             = "Europe/Amsterdam"

  target_groups = {
    "tg1" = {name = "tetris",  protocol = "HTTP", port = "8090", path = "/", matcher = "200-399"}
  }

  listener_rules = {
    "rule1"    = {tg = "tg1", application_type = "TETRIS", path_pattern = "/*", cognito = true}
  }

  ssm_passwords = {}

  ecs_ctr_fes_1_max_instance_size     = "2"
  ecs_ctr_fes_1_instance_type         = "c5.large"

  ecs_capacity_providers = {
    "01" = {description = "On_demand_capacity", number = "001", asg = "01"}
  }

  ecs_autoscaling_group = {
    "01" = {description = "On_demand_capacity", number = "001", spot = false}
  }

}
