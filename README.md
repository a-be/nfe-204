# nfe-204
Hackathon NFE-204
Ce repo contient l'ensemble des resources nécessaires à la réalisation du Hackathon NFE-204.

## Flux de données (Patrick)

todo ajouter les instructions...

## Kafka (Marc)


[Virtualbox](https://www.virtualbox.org/wiki/Downloads)
[Vagrant](https://www.vagrantup.com/downloads.html)

Ces deux outils qui permettent, l'un de construire des machines virtuelles, et l'autre de scripter leur création
j'ai essayé de mettre au point une VM qui ressemble à celle du CNAM : une Opensuse.
Mais je ne connais pas la version exacte de l'OS des opensuse du CNAM.
cela dit, si vous placez ce script dans un fichier nommé Vagrantfile

```
Vagrant.configure("2") do |config|
  config.vm.box = "bento/opensuse-leap-42.3"
  config.vm.box_version = "201710.25.0"  
  config.vm.post_up_message = "Open suse 42.3, demarree"
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox", disabled: "true"
  # Machine
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
    vb.name = "Opensuse 42.3"
  end
  # Provisionning 
  config.vm.provision "shell", inline: <<-SHELL
    # Installation JDK 8
    sudo zypper -n in java-1_8_0-openjdk
    sudo zypper -n in java-1_8_0-openjdk-devel
    
    # Installation Client Kafka sur Python
    sudo apt-get update    
    sudo apt-get install -y python3
    sudo apt-get install -y python-kafka-python
    
    # Installation de mysql
    sudo zypper -n install mysql-community-server
    sudo insserv mysql
    sudo rcmysql start
    # Note : un ` mysql -u root` suffira à entrer dans la session mysql, non sécurisée.
  SHELL
end
```

1. et que vous tapez : vagrant up
1. une machine virtuelle sera créé,
1. accessible par un : vagrant ssh

Rien ne dit qu'ensuite au CNAM les sudo apt-get nous seront accessibles (ils sont peut-être interdits et il faudra tout obtenir par des tar -xvf),
mais ça permet d'avoir un système de base où si l'on y essaie des commandes et que l'on fait des manips,
on pourra à peu près faire celles-là aussi, le jour dit.

J'ai poursuivi mes expériences.
Voici l'installation et l'expérimentation du bon fonctionnement de Kafka.

Là, je ne suis pas parvenu à tout faire en automatique parce que Suse n'accepte pas une commande que je connaissais sous Debian, et je n'ai pas réussi à avoir son équivalent.
Donc il y a quelques opérations manuelles à faire

```
    # Download et décompression de Kafka 2.11
    # d'après https://kafka.apache.org/quickstart
    wget 'http://apache.crihan.fr/dist/kafka/1.0.0/kafka_2.11-1.0.0.tgz' -O kafka.tgz
    tar -xvf kafka.tgz
```
Ca, je l'ai expérimenté d'après le lien
http://kafka.apache.org/documentation.html


## Config Spark (Habib)

todo ajouter les instructions...

## Connexion Kafka/Spark (Youness)

#### Paramétrage zookeeper.properties (machines de test : samar01, samar03, présentées par leurs IP)

```bash
# the directory where the snapshot is stored.
dataDir=/tmp/zookeeper
# the port at which the clients will connect
clientPort=2080
# disable the per-ip limit on the number of connections since this is a non-production config
initLimit=5
syncLimit=2

server.1=163.173.230.131:2888:3888
server.2=163.173.230.133:2888:3888
```

##### Paramétrage server.properties

```bash
# see kafka.server.KafkaConfig for additional details and defaults

############################# Server Basics #############################

# The id of the roker. This must be set to a unique integer for each broker.
broker.id=2

############################# Socket Server Settings #############################

# The address the socket server listens on. It will get the value returned from
# java.net.InetAddress.getCanonicalHostName() if not configured.
#   FORMAT:
#     listeners = listener_name://host_name:port
#   EXAMPLE:
#     listeners = PLAINTEXT://your.host.name:9092
#listeners=PLAINTEXT://:9092

host.name=samar03.cnam.fr
zookeeper.connect=163.173.230.131:2080,163.173.230.133:2080
```


1. Launch Zookeeper
```bin/zookeeper-server-start.sh config/zookeeper.properties```
1. launch Kafka
``` bin/kafka-server-start.sh config/server.properties```
1. Create a topic
```bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test```
1. Create producer
```bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test```
1. Create consumer
```bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning```
1. Launch Spark
```spark-shell```

## Workflow (Ramsy)

## Visualisation (Romy)

Ce qui a été fait :

1. Faire le nuage de mot 
1. et taille de la police en fonction des occurences
1. Lecture d’un fichier json en temps réel en AJAX

Sources :

* Nuage de mots : https://timdream.org/wordcloud2.js/#love
* AJAX : http://christele.developpez.com/tutoriels/ajax/ajax-en-clair/
* websocket : https://github.com/Pithikos/python-websocket-server?files=1
* websocket scala : https://www.xul.fr/html5/websocket.php
* websocket tweet : https://romain-gervais.fr/articles/afficher-10-ecrire-une-application-temps-reel-en-5-minutes.html
* zookeeper : http://macappstore.org/zookeeper/

Comment l'utiliser : 

1. Importer les scripts  
1. Récupérer l’adresse de sortie du fichier de mots et occurrences
1. dans plot.js, Changer la ligne 35 : xmlhttp.open("GET", "https://api.myjson.com/bins/9mypv", true);
1. Ouvrir dans un navigateur le fichier index.html

Installer kafka et Spark en local sous macOS:

source : https://dtflaneur.wordpress.com/2015/10/05/installing-kafka-on-mac-osx/

```
# Install 
brew search kafka
brew install kafka

# kafka 
zkserver start
# -- aller dans le répertoire kafka
cd /usr/local/Cellar/kafka/1.0.0/bin
kafka-server-start /usr/local/etc/kafka/server.properties
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
#kafka-topics --zookeeper localhost:2181 --replica 1 --partition 1 --topic test
kafka-console-producer --broker-list localhost:9092 --topic test
kafka-console-consumer --zookeeper localhost:9092 --topic test --from-beginning

# construire le .jar
brew install sbt
o=$( pwd )
mkdir src/main/scala
cd src/main/scala
vim KafkaWordCount.scala # voir https://github.com/a-be/nfe-204/blob/master/Workflow
cd $o
vim build.sbt # voir https://github.com/a-be/nfe-204/blob/master/build.sbt
sbt package
ls ./target/scala-2.11/*.jar
# Lancer l'execution
spark-submit  --packages org.apache.spark:spark-streaming-kafka-0-8_2.11:2.2.1 --class "KafkaWordCount" --master local[4] target/scala-2.11/spark-kafka-project_2.11-1.0.jar localhost:2181 1 test 1

```


## Je jour J

Importer les fichiers

```
git clone https://github.com/a-be/nfe-204.git
cd nfe-204
```

Install kafka sur chaque machine

```
wget 'http://apache.crihan.fr/dist/kafka/1.0.0/kafka_2.11-1.0.0.tgz' -O kafka.tgz
tar -xvf kafka.tgz
```

Construire le workflow

```
o=$( pwd )
mkdir -p src/main/scala
cp nfe-204/Workflow src/main/scala/KafkaWordCount.scala
cp nfe-204/build.sbt .
sbt package
ls ./target/scala-2.11/*.jar
```

Récuperer l'adress ip de tout les machines
```
ip a | grep 'inet 163.173.*'
```


Configurer le spark pour que ca soit distribué
source : http://blog.ippon.fr/2014/11/20/utiliser-apache-spark-en-cluster/

```

    val kafkaParams = Map[String, Object](
	"bootstrap.servers" -> "localhost:909,anotherhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],

      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )


    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](Seq(topics), kafkaParams)
)
```


Importer les tweets
```
scp samar01:/tmp/extract_tweets_5000000.json /tmp
```

Lancer le flux sur une des marchines
```
java -jar tweet-publisher -s "samar01:9092 samar02:9092 samar03:9092 samar04:9092 samar05:9092" -t tweetGroup1 -f /tmp  -d 1000  -l 
```
