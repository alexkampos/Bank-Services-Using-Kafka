<h3>TEST APP</h3>
<br>
You can test the app using postman.
A user is created automatically when you start the app with email/username "test_email_1" and balance amount 1500.
Below you can find info regarding those calls:
<br>
<br>
DEPOSIT CALL:
<br>
Authorization:
<br>
username: test_email_1
<br>
password: 1234
<br>
Body:
<br>
{
    "userEmail": "test_email_1",
    "userAction": "DEPOSIT",
    "amount": (the amount you want as number)
}
<br>
WITHDRAW CALL:
<br>
Authorization:
<br>
username: test_email_1
<br>
password: 1234
<br>
Body:
<br>
{
    "userEmail": "test_email_1",
    "userAction": "WITHDRAW",
    "amount": (the amount you want as number)
}

<h1>RUN APPS WITH DOCKER</h1>
<br>
<span style="color:  #c5f015"> 1. Git clone Kafka and Zookeeper images: <br> </span>
On a personal preferred folder do: 
<strong>git clone https://github.com/conduktor/kafka-stack-docker-compose.git</strong>
<br>
<br>
<span style="color:  #c5f015"> 2. Start single instances of Kafka and Zookeeper: </span>
<br>
On the previously mentioned folder run: <strong>docker-compose -f zk-single-kafka-single.yml up -d</strong>
<br>
<br>
<span style="color:  #c5f015"> 3. Build main app with maven: </span>
<br>
Inside main app project folder run: <strong>mvn clean package -DskipTests</strong>
<br>
<br>
<span style="color:  #c5f015"> 4. Copy main app jar in Dockerfile's folder: </span>
<br>
Run: <strong>cp (<i>path to target folder of main app</i>)/target/MarlowBank-1.0-SNAPSHOT.jar (<i>path to src folder of main app</i>)/src/main/docker</strong>
<br>
<br>
<span style="color:  #c5f015"> 5. Run main app and postgresql images: </span>
<br>
Inside the docker folder run: <strong>docker-compose up</strong>
<br>
<br>
<span style="color:  #c5f015"> 6. Build consumer app with maven: </span>
<br>
Inside consumer app project folder run: <strong>mvn clean package -DskipTests</strong>
<br>
<br>
<span style="color:  #c5f015"> 7. Copy consumer app jar in Dockerfile's folder: </span>
<br>
Run: <strong>cp (<i>path to target folder of consumer app</i>)/target/MarlowBankConsumer-1.0-SNAPSHOT.jar (<i>path to src folder of consumer app</i>)/src/main/docker</strong>
<br>
<br>
<span style="color:  #c5f015"> 8. Run consumer app image: </span>
<br>
Inside the docker folder run: <strong>docker-compose up</strong>
