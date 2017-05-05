SbtTestTasks="coverage lock test it:test coverageReport coverageAggregate scalastyle"

alias \
  sbt="${0:a:h}/sbt.launcher" \
  sbtTestAll="sbt $SbtTestTasks " \
;
