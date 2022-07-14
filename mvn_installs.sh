# Processing 3
#mvn install:install-file -Dfile=lib/lx-0.2.2-jar-with-dependencies.jar -DgroupId=heronarts -DartifactId=lx -Dversion=0.2.2 -Dpackaging=jar
#mvn install:install-file -Dfile=lib/p3lx-0.2.2.jar -DgroupId=heronarts -DartifactId=p3lx -Dversion=0.2.2 -Dpackaging=jar
#mvn install:install-file -Dfile=lib/lxstudio-0.2.2.jar -DgroupId=heronarts -DartifactId=lxstudio -Dversion=0.2.2 -Dpackaging=jar
#mvn install:install-file -Dfile=lib/processing-3.5.4/core.jar -DgroupId=org.processing -DartifactId=core -Dversion=3.5.4 -Dpackaging=jar

mvn install:install-file -Dfile=lib/firmata4j-2.3.8-SNAPSHOT.jar -DgroupId=com.github.kurbatov -DartifactId=firmata4j -Dversion=2.3.8-SNAPSHOT -Dpackaging=jar

# Processing 4
mvn install:install-file -Dfile=lib/lx-0.4.1-SNAPSHOT-jar-with-dependencies.jar -DgroupId=heronarts -DartifactId=lx -Dversion=0.4.1-SNAPSHOT-jar-with-dependencies -Dpackaging=jar
mvn install:install-file -Dfile=lib/p4lx-0.4.1-SNAPSHOT.jar -DgroupId=heronarts -DartifactId=p4lx -Dversion=0.4.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=lib/lxstudio-0.4.1-SNAPSHOT.jar -DgroupId=heronarts -DartifactId=lxstudio -Dversion=0.4.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=classpath/core-4.0b8.jar -DgroupId=org.processing -DartifactId=core -Dversion=4.0b8 -Dpackaging=jar
mvn install:install-file -Dfile=classpath/jogl-all-4.0b8.jar -DgroupId=org.jogamp.jogl -DartifactId=jogl-all -Dversion=4.0b8 -Dpackaging=jar
mvn install:install-file -Dfile=classpath/gluegen-rt-4.0b8.jar -DgroupId=org.jogamp.gluegen -DartifactId=gluegen-rt-main -Dversion=4.0b8 -Dpackaging=jar
