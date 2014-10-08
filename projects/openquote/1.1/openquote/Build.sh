CLASSPATH=$(for i in ../development/build/lib/*.jar; do echo -n $i:; done)

ant $*