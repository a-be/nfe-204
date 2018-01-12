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
et que vous tapez : vagrant up
une machine virtuelle sera créé,
accessible par un : vagrant ssh
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
Launch Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties
1. launch Kafka
bin/kafka-server-start.sh config/server.properties
1. Create a topic
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
1. Create producer
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
1. Create consumer
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
1. Launch Spark
spark-shell

## Workflow (Ramsy)

## Visualisation (Romy)

Importer les scripts  : git clone https://rchenmintao@bitbucket.org/rchenmintao/nfe204_201709_hackathon.gitaller
 La branche avec AJAX et WorldCloud : git fetch && git checkout wordCloud
Récupérer l’adresse de sortie du fichier de mots et occurrences
Changer la ligne 35 : xmlhttp.open("GET", "https://api.myjson.com/bins/9mypv", true);
Ouvrir dans un navigateur le fichier index.html

