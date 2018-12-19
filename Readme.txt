cd DC/SafePathServer/SafePath
make
rmiregistry 4444&

java -cp ".:mysql-connector-java-5.1.46.jar" -Djava.security.policy=policy dbServer.SafePathDB
java -cp ".:java-json.jar" -Djava.security.policy=policy Server.SafePathServer 1
java -cp ".:java-json.jar" -Djava.security.policy=policy Server.SafePathServer 2
java -Djava.security.policy=policy LoadBalancer.LoadBalancer
java -Djava.security.policy=policy Client.Client


https://maps.googleapis.com/maps/api/directions/json?origin=kroger,%20indianapolis&destination=wallmart%20indianapolis&key=AIzaSyCqr7zwIzy9ZCVvpa43iE8kc58oJv5m5Bc&alternatives=true
