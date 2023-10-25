#!/bin/bash
rm -r artifacts/
ant -f build-21.xml clean
ant -f build-21.xml
mkdir -p artifacts/jar
cp release/sunflow.jar artifacts/jar/sunflow-0.07.2.jar
cp release/janino.jar artifacts/jar/janino-2.5.15.jar
cp sanity/dacapo-9.12-MR1-bach.jar artifacts/dacapo-modified.jar
cp sanity/dacapo-9.12-MR1-bach.jar artifacts/dacapo-modified.jar
cp sanity/poa.jar artifacts/poa.jar
cd artifacts/
jar -uf dacapo-modified.jar jar/janino-2.5.15.jar
jar -uf dacapo-modified.jar jar/sunflow-0.07.2.jar
# /usr/lib/jvm/java-8-openjdk-amd64/bin/java -javaagent:poa.jar -jar dacapo-modified.jar sunflow -n 100
