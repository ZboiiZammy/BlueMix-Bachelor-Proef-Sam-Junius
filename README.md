#IBM BlueMix : The Mobile Cloud

######*Bachelor Proef door Sam Junius*

##Project Voorstelling

###Doelstelling

In deze bachelorproef werk je een beperkte applicatie op het IBM BlueMix platform, meer bepaald met de boilerplate “Mobile Cloud”. Je onderzoekt hierbij de voor- en nadelen van dit RAD-Cloud-framework (Rapid Application Development) van IBM. Je zal je toespitsen op het Mobile Cloud gedeelte, slechts een onderdeel van BlueMix dat een heel scala aan ontwikkelingsmogelijkheden aanbiedt.

###Inhoud

BlueMix is een implementatie van de Open Cloud Architecture van IBM, waarbij ontwikkelaars in staat worden gesteld om op korte tijd kwaliteitsvolle applicaties te creëren, deployen en nadien ook te beheren. Hierbij biedt IBM een breed scala aan services en technologieën aan, die je naar hartenlust kan combineren. Dankzij het brede portfolio dat IBM via BlueMix aanbiedt is er voor nagenoeg elke applicatie een combinatie voorhanden.

In deze bachelorproef zal je je specifiek verdiepen in de zogenaamde “Mobile Cloud” boilerplate. Dit is een combinatie van onderstaande technologieën en services:
* **SDK for Node.js** geeft de mogelijkheid om mobiele en webapplicaties te maken door gebruik te maken van de mobile app template en SDK voor Node.js
*	**Mobile Application Security** laat toe om de toegang tot de mobile cloud services te gaan restricteren en beveiligen.
*	**Push** is een component waarmee je eenvoudig push-berichten naar mobiele toestellen kan sturen
*	**Mobile Data** biedt SDK’s aan om binnen Android, iOS en JavaScript de back-end aan te spreken op een zeer transparante en eenvoudige manier.

De Mobile Cloud boilerplate wordt meteen in een reële case toegepast. Je zal een applicatie ontwikkelen die de uitwisseling van bedrijfskennis mogelijk maakt. Een gebruiker kan hierbij een nieuwe best practice delen met collega’s omtrent bepaalde topics. Wanneer een werknemer ingeschreven is op een topic zal hij/zij een notification ontvangen wanneer nieuwe best practices werden toegevoegd in deze categorie. Aangezien het om bedrijfsgevoelige info gaat is beveiliging een must.

###Eindresultaten

Het eindresultaat van deze bachelorproef bestaat uit volgende onderdelen:
* een kritische analyse van het BlueMix platform, meer bepaald de Mobile Cloud boilerplate
* een technische uiteenzetting van de mogelijkheden van de Mobile Cloud boilerplate
* een uitgewerkte applicatie (zie inhoud) met behulp van Mobile Cloud in BlueMix:
  * Node.js
  * iOS
  * Android

##Logboek

* **22/09/2014** - Voorstel ingestuurd
* **23/09/2014** - Onderzoek naar Bluemix. Kijken of ik het kan opstarten. Het feit is dat alles geld kost. Ik kan enkel een 30dagen account registreren. Ik heb een mail gestuurd naar VR om te zien of ik een account kan verkrijgen die langer meegaat. Ik heb het project aangemaakt met het boiler template van het type Mobile Cloud. Bluemix heeft direct een hostname opgezet voor alles.
  <img src="http://i.imgur.com/AkFJ7gJ.png"/> 
  Ik zit nu vast met hoe ik nu verder moet. Dit ligt vooral aan hoe Node.js, MAS, Mobile Data en Push in elkaar steken.
* **15/10/2014** - Ik heb de tutorial gevolgd van Android en heb succesvol een android app kunnen opzetten. Alles is heel goed uitgelegd!
  <img src="http://i.imgur.com/TaETooG.png"/>
  <img src="http://i.imgur.com/cg6PSH1.png"/>
  Ook heb ik een node.js project kunnen opzetten met bluemix alsook een GIT repository op Jazz. Dit is een versioningsysteem van IBM die ervoor zorgt dat de app kan blijven runnen in samenwerking met BlueMix.
  <img src="http://i.imgur.com/24ZI7sJ.png"/>
* **21/10/2014** - Telefonisch contact gehad met de mensen van IBM betreffende het verlopen van mijn account. Ik moet kredietkaart gegevens invullen en dan kan ik doorwerken. Eventueel vragen aan mn ouders ofwel een prepaid kredietkaart. De persoon van IBM heeft mij ook verschillende documenten en filmpjes gemailed om mij te ondersteunen bij het maken van mijn applicatie.
* **30/10/2014** - Meeting gehad met Mvr. Bruylandt om te beslissen welke applicaties ik moet maken in welke talen of welke platformen. We hebben besloten om een applicatie te maken voor de volgende platformen: iOS, Android & Node.js
*  **11/12/2014** - Vandaag opleiding gaan volgen bij IBM. HEEL VEEL bijgeleerd over de kracht van het systeem en verschillende oefeningen gemaakt om blueMix te leren kennen. Zowel via commandline als via de UI. Ook het idee gekregen om tijdens mijn presentatie live een applicatie te bouwen. Waarschijnlijk in android. Wel op voorhand opnemen zodat alles vlot verloop tijdens de presentatie zelf en er geen plotse onnodige fouten opkomen. Ook contact gehad met Abdoul Gadiri D. voor het nalezen van mijn bachelorproef.

  >Abdoul Gadiri D. is an IT Specialist within the Software Group division of IBM. He graduated from the Universite Libre de Bruxelles as an engineer in computer sciences. His main focus areas includes Application Integration Middleware and IBM Mobile Foundation.
* **24/01/2015** - opzetten van een github repository en opbouwen van het logboek. Ik heb besloten om aan de Android applicatie te werken als eerste, aangezien deze taal mij het beste ligt. Hierdoor kan ik de algemene structuur, alsook layout die ik wil verkrijgen opbouwen voor de applicatie.
* **25/01/2015** - vandaag verder werken aan de Android app. De tutorial (http://www.ibm.com/developerworks/library/mo-android-mobiledata-app/index.html) toch nog maar eens gevolgd. Van alle services versta ik nu veel beter hoe Mobile Data werkt. Push lijkt me ook wel slim in elkaar te zitten maar dit is pas voor later als ik een app werkende krijg. MobileSecurity moet ik nog beter bekijken. Eerst focus op de opbouw van de applicatie. Vandaag ook het plan om uit te tekenen op papier hoe ik de app wil laten werken. Hoe node.js cloud services hier bij komt te werken blijft me wel nog even een raadsel.
* **02/02/2015** - Android app verder bekijken. Eerste scherm bijna afgewerkt. Custom ListAdapter geschreven. De bedoeling is om op scherm 1 de topics te tonen. Dan zo doorklikken om een lijst met alle discussies te zien van dat bepaald topic en de mogelijkheid om te subscriben. Problemen tegengekomen met de versies van de .jar files (BlueMix SDK). Blijkbaar is de versie van de huidige IBM BlueMix software anders dan die van het skeleton applicatie. (Heb nog al gemerkt dat veel code in hun tutorials en documentatie out of date is) Nu bezig met wat "layout" te fixen. Gebruik aan het maken van de Picasso Image Loader (http://square.github.io/picasso/) om aan elke topic een afbeelding te geven zodat de gebruiker dit beter kan plaatsen waarover de topic gaat. 
* **03/02/2015** - De aplicatie getest op een echt device (Acer Tablet) en zeer tevreden van het resultaat. Ook verder gekeken naar het inladen van de afbeeldingen op de Android applicatie. De Picaso library laten vallen en code gevonden die de standaard code van Android gebruikt om een inputStream binnen te halen. Dit zorgt er voor dat ik nu afbeeldingen heel simpel en snel kan inladen. Dit zorgt er voor dat het eerste scherm zo goed als afgewerkt is en levert dit resultaat op:
  <img src="http://i.imgur.com/IfjnIOy.jpg"/>
Ook is er even de opbouw bekeken van de hele applicatie en dit geeft dit resultaat: (schets)
  <img src="http://i.imgur.com/pqSw1TM.jpg"/>
*  **04/02/2015** - de iOS applicatie bekeken voor de eerste keer. Heb wat zaken op mijn Macbook moeten instaleren zoals 'Pods' maar heb deze app veel sneller werkende gekregen als de Android paplicatie. Heb zoals bij de android applicatie de skeleton applicatie opgebouwd zodat het concept tegemoet komt. Buiten de afbeeldingen werkt alles al. Volgende is het focussen op het 2e scherm.
* **05/02/2015** - Het scherm ontwikkeld voor het toevoegen van een 'Best Practise'
* **06/02/2015** - Het scherm voor het toevoegen van een 'Best Practise' ontwikkeld. Nu blijkt dat na het toevoegen van een BP Object dit WEL terecht komt in mijn databank maar niet op het scherm dat alle BP's moet tonen van dat topic.
* **07/02/2015** - Verder gekeken naar waarom de BP's niet tonen. Geen oplossing gevonden..
* **09/02/2015** -
