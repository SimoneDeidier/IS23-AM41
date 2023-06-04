# Software Engeneering - Final Project 2023

Professor: Alessandro Margara<br />
Group name: **IS23-AM41**<br />
Students:

* [D'Alessio Edoardo - edoardo.dalessio@mail.polimi.it](https://github.com/EdoardoDAlessio)
* [De Ciechi Samuele - samuele.deciechi@mail.polimi.it](https://github.com/Samdec01)
* [Deidier Simone simone.deidier@mail.polimi.it](https://github.com/SimoneDeidier)
* [Ermacora Iacopo - iacopo.ermacora@mail.polimi.it](https://github.com/IacopoErmacoraPolimi)

## Objectives

- [x] Simple rules
- [x] Full rules
- [x] Text User Interface
- [x] Graphic User Interface
- [x] RMI connection
- [x] Socket connection
- [ ] Multiple matches
- [x] Persistence
- [x] Disconnection resilience
- [x] Chat

### Works status

|Feature name|Status|Percentage status|Comments|
|:-:|:-:|:-:|:-:|
|Simple rules|Done|100%||
|Full rules|Done|100%||
|TUI|Work in progress|75%|We are implementing some missing game screens|
|GUI|Bug fixing|99%||
|RMI|Done|100%||
|TCP|Done|100%||
|Multiple matches|Not implemented|0%|We chose to not implement this advanced feature|
|Server persistance|Done|100%||
|Disconnection resilience|Done|100%||
|Chat|Done|100%||

* *Latest update: 04/06/2023*

## Unified Modeling Language

The first step for this course is to design the *UML* of the game with the mandatory implementation of the *Model-View-Controller design pattern*. All the UML's are published and available to inspect, they can be found in the ***deliveries/UML*** folder. 

## *Peer-reviews*

During this course, we will have to review some documents of other's groups. Our group will also be reviewd, and to help other students understanding our works and our ideas, we will create some *"description documents"*. All the documents (*peer-reviews and description*) are published, in fact:

* All the *peer-review documents* are available to read in the ***deliveries/peer-reviews/reviews*** folder.
* All the *description documents* are available to read in the ***deliveries/peer-reviews/documents*** folder.

## Code testing

All the code from the *Model classes* and the most important from the *Server-Controller classes* has been tested with ***JUnit***, a powerful suite for the code testing and validation. Our test coverage:

* Number of tests: **40.109** - *all passed*

|*MVC* Classes|Class coverage|Method coverage|Lines of code coverage|
|:-:|:-:|:-:|:-:|
|Model|100%|99%|93%|
|Server-Controller|29%|40%|23%|

* *Latest update: 04/06/2023*