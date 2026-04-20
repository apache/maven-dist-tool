# Plugins Build Results for Maven 4 Compatibility Check

WIP (should be in Maven 4.0.0-RC6):

- [PR #11868](https://github.com/apache/maven/pull/11868) for `maven-source-plugin`
- [PR #11869](https://github.com/apache/maven/pull/11869) for `plugin-tools`

## core

|                  | 3.9.15 | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.x |
| ---------------- | -------- | -------- | -------- | -------- |
| [maven-clean-plugin](https://github.com/apache/maven-clean-plugin/tree/maven-clean-plugin-3.x) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-compiler-plugin](https://github.com/apache/maven-compiler-plugin/tree/maven-compiler-plugin-3.x) | :white_check_mark: | :white_check_mark: | :x: | :x: |
| [maven-deploy-plugin](https://github.com/apache/maven-deploy-plugin/tree/maven-deploy-plugin-3.x) | :white_check_mark: | :x: | :x: | :x: |
| [maven-install-plugin](https://github.com/apache/maven-install-plugin/tree/maven-install-plugin-3.x) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-resources-plugin](https://github.com/apache/maven-resources-plugin/tree/maven-resources-plugin-3.x) | :white_check_mark: | :x: | :x: | :x: |
| [maven-site-plugin](https://github.com/apache/maven-site-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-verifier-plugin](https://github.com/apache/maven-verifier-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [surefire](https://github.com/apache/maven-surefire/tree/HEAD) | :x: | :x: | :x: | :x: |

## packaging

|                  | 3.9.15 | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.x |
| ---------------- | -------- | -------- | -------- | -------- |
| [maven-acr-plugin](https://github.com/apache/maven-acr-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-ear-plugin](https://github.com/apache/maven-ear-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-ejb-plugin](https://github.com/apache/maven-ejb-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :x: | :x: |
| [maven-jar-plugin](https://github.com/apache/maven-jar-plugin/tree/maven-jar-plugin-3.x) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-jlink-plugin](https://github.com/apache/maven-jlink-plugin/tree/master) | :x: | :x: | :x: | :x: |
| [maven-jmod-plugin](https://github.com/apache/maven-jmod-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-rar-plugin](https://github.com/apache/maven-rar-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-shade-plugin](https://github.com/apache/maven-shade-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-source-plugin](https://github.com/apache/maven-source-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-war-plugin](https://github.com/apache/maven-war-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |

## reporting

|                  | 3.9.15 | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.x |
| ---------------- | -------- | -------- | -------- | -------- |
| [jxr](https://github.com/apache/maven-jxr/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-changelog-plugin](https://github.com/apache/maven-changelog-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-changes-plugin](https://github.com/apache/maven-changes-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-checkstyle-plugin](https://github.com/apache/maven-checkstyle-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-doap-plugin](https://github.com/apache/maven-doap-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-javadoc-plugin](https://github.com/apache/maven-javadoc-plugin/tree/master) | :x: | :x: | :x: | :x: |
| [maven-jdeps-plugin](https://github.com/apache/maven-jdeps-plugin/tree/master) | :x: | :x: | :x: | :x: |
| [maven-pmd-plugin](https://github.com/apache/maven-pmd-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-project-info-reports-plugin](https://github.com/apache/maven-project-info-reports-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |

## tools

|                  | 3.9.15 | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.x |
| ---------------- | -------- | -------- | -------- | -------- |
| [archetype](https://github.com/apache/maven-archetype/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :x: |
| [enforcer](https://github.com/apache/maven-enforcer/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-antrun-plugin](https://github.com/apache/maven-antrun-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-artifact-plugin](https://github.com/apache/maven-artifact-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-assembly-plugin](https://github.com/apache/maven-assembly-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-dependency-plugin](https://github.com/apache/maven-dependency-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-gpg-plugin](https://github.com/apache/maven-gpg-plugin/tree/master) | :white_check_mark: | :x: | :white_check_mark: | :white_check_mark: |
| [maven-help-plugin](https://github.com/apache/maven-help-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-invoker-plugin](https://github.com/apache/maven-invoker-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-jarsigner-plugin](https://github.com/apache/maven-jarsigner-plugin/tree/master) | :x: | :x: | :x: | :x: |
| [maven-jdeprscan-plugin](https://github.com/apache/maven-jdeprscan-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-remote-resources-plugin](https://github.com/apache/maven-remote-resources-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-scm-publish-plugin](https://github.com/apache/maven-scm-publish-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [maven-scripting-plugin](https://github.com/apache/maven-scripting-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-stage-plugin](https://github.com/apache/maven-stage-plugin/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [maven-toolchains-plugin](https://github.com/apache/maven-toolchains-plugin/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [plugin-tools](https://github.com/apache/maven-plugin-tools/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| [release](https://github.com/apache/maven-release/tree/master) | :white_check_mark: | :x: | :x: | :x: |
| [scm](https://github.com/apache/maven-scm/tree/master) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |

