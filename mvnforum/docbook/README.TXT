You can use docbook on Linux or Window 

- I use a package at http://www.cs.put.poznan.pl/dweiss/xml/projects/ant-docbook-styler/index.xml?lang=en
+ Download and unzip the styler to one directory.
+ Set DOCBOOK_HOME environment variable refer to that directory.
+ Change to that directory and run 
    ant -buildfile ant-build-docbook.xml
    
+ You must modify some files IN MVNFORUM
//CatalogManager.properties
...
catalogs=your_docbook_folder
..

// mvnforum.xsl : Modify in line refer to your docbook.xsl in your $DOCBOOK_HOME directory
....
<xsl:import href="your_docbook_folder/xsl/html/docbook.xsl" />
....




// Modify 1 line in files : user.xml , install.xml, faq.xml.
Path point to file docbookx.dtd in your docbook directory as

....
<!DOCTYPE book
  PUBLIC "-//OASIS//DTD DocBook XML V4.2//EN" "/mnt/sda6/dev/softs/docbook/dtd/docbookx.dtd"
  [
    <!ENTITY % global.entities SYSTEM "./include/global.xml">
    %global.entities;
    <!ENTITY user1 SYSTEM "user_chap1.xml">

    <!ENTITY % entities SYSTEM "entities.xml">
    %entities;
  ]
>
....

    * Path to file docbookx.dtd in your docbook
        file:///mnt/sda6/dev/softs/docbook/docbook/dtd/docbookx.dtd 
    * Path to file global.xml
        file:///mnt/sda6/dev/mvn/dev/projects/mvnforum/docbook/repository/include/global.xml


