import sys
import os
process=5
if len(sys.argv)==3:
    process=int(sys.argv[2])

os.system"git clone")
os.system("mvn:package")
for i in range(1, process):
    os.system("java -jar target/ETLExample-1.0-SNAPSHOT-jar-with-dependencies.jar")
