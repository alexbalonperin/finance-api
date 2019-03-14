LOCAL_BIN=/usr/local/bin

curl -Lo $LOCAL_BIN/coursier https://git.io/coursier-cli && chmod +x $LOCAL_BIN/coursier

coursier bootstrap org.scalameta:scalafmt-cli_2.12:2.0.0-RC5 \
  -r sonatype:snapshots \
  -o $LOCAL_BIN/scalafmt --standalone --main org.scalafmt.cli.Cli
