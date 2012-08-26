DIR=`dirname $(readlink -f $0)`

java -cp "$DIR/:$DIR/*" org.jooq.util.GenerationTool /jooq_config.xml
