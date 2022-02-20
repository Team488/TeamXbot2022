# FRC 2022 Robot Code [![Build Status](https://dev.azure.com/Team488/Team%20488%20Builds/_apis/build/status/Team488.TeamXbot2022?branchName=main)](https://dev.azure.com/Team488/Team%20488%20Builds/_build/latest?definitionId=8&branchName=main)

Rapid React!

## Pre-commit hooks

To help validate your code at commit time, you can enable the pre-commit hooks locally by:
- have python installed on your system
- run `pip install pre-commit`
- At the root of this repository run `pre-commit install`

If they are ever getting in your way you can always force a commit to go through by adding the `--no-verify` flag, eg: `git commit --no-verify -am "This is broken and I want it merged anyway"`