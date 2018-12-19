JFLAGS = -g
JC = javac -cp ".:java-json.jar:mysql-connector-java-5.1.46.jar"
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
Server/ISafePathServer.java\
Server/SafePathServer.java\
Server/SafePathServerImpl.java\
Server/GooglePath.java\
Server/Incidents.java\
Server/Steps.java\
Server/Route.java\
Server/Session.java\
Server/AppServer.java\
dbServer/IDBSafePathServer.java\
dbServer/IncidentCalculation.java\
dbServer/SafePathDB.java\
LoadBalancer/ILoadBalancer.java\
LoadBalancer/LoadBalancer.java\
LoadBalancer/LoadBalancerImpl.java\
Client/Client.java\
dbServer/dbServer.java\

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) */*.class