mvn clean install
scp target/critter-1.0-SNAPSHOT.zip emicklei@boldapp26.dev.bol.com:/tmp/critter.zip
ssh emicklei@boldapp26.dev.bol.com

# sudo su - critter
# cd bin && sh stop.sh && cd ..
# unzip -d /opt/critter -x conf /tmp/critter.zip 

# "sudo su - critter && unzip /tmp/critter.zip -d /opt/critter -d conf"
# && sudo su - critter && export PATH=${PATH}:/usr/jdk1.6.0_22/bin"