LightBulb Game
==============

Tímový projekt - logická hra inšpirovaná hrou LightBulb pre Android.

Použité technológie:
- Java SE 21
- JavaFX
- Maven

Autori:
- Adam Varsányi - xvarsa01 - veduci
- Adam Havlík - xhavli59

Popis projektu:
---------------
LightBulb Game je logická hra, kde je cieľom prepojiť zdroj energie so všetkými žiarovkami otáčaním políčok na hracej ploche.
Každé políčko môže obsahovať vodič, zdroj alebo žiarovku. Hráč kliknutím otáča políčka o 90 stupňov doprava
a snaží sa vytvoriť správne zapojenie, aby sa rozsvietili všetky žiarovky.

Funkcionality:
--------------
- Hracia doska s M x N políčkami
- Možnosť výberu obtiažnosti (easy, medium, hard)
- Farebné rozlíšenie pripojených a nepripojených políčok
- Počítanie počtu ťahov a uplynutého času
- Pauza a pokračovanie v hre
- Vitazné okno po úspešnom vyriešení
- Informačné okno s n8povedou a počtom potrebných otočení pre každé políčko
- Logovanie priebehu hry do súboru
- Krokovanie aktuálnej hry dopredu aj dozadu
- Undo a Redo ťahy
- Domovská stránka s voľbou obtiažnosti a skóre
- Podpora náhodne zvolených farebných motívov - pri odovzdaní obmadzené koli velkosti

Použité návrhové vzory:
-----------------------

1. **Observer Pattern**
   - **Použitie:** Triedy `Game` a `GameNode` implementujú observer pattern prostredníctvom rozhrania `Observable` a triedy `AbstractObservable`.
   - **Účel:** Umožňuje automaticky informovať všetky grafické komponenty (napríklad `NodeView`, `InfoPanel`), keď sa zmení stav herného modelu.
   - **Výhoda:** GUI sa aktualizuje automaticky bez potreby explicitného volania metód na každom mieste kódu.
   - **Príklad:** Keď hráč otočí políčko, `GameNode` notifikujú `NodeView`, aby sa graficky prekreslili.

2. **Model-View-Controller (MVC)**
   - **Použitie:**
     - **Model:** Triedy `Game`, `GameNode`, `MapGenerator`, `Position`
     - **View:** `NodeView`, `GamePage`, `InfoPanel`, `PausePanel`, `WinPanel`
     - **Controller:** `Navigation`, logika v `GamePage` (napr. spracovanie kliknutí)
   - **Účel:** Oddelenie dát (model), zobrazenia (view) a logiky ovládania (controller).
   - **Výhoda:** Lepšia údržba, možnosť meniť UI bez zásahu do logiky, jednoduchšie testovanie.

Struktura projektu:
-------------------
src/         zdrojové súbory
lib/         externé obrázky a súbory
data/        uložené dáta a logy
readme.txt   tento dokument
requirements.pdf špecifikácia požiadaviek
pom.xml      konfigurácia Maven

Spustenie:
----------
Spustenie:
1. Preklad a vytvorenie .jar archívu:
   mvn clean package

   Výstupný súbor:
   target/lightbulb-game.jar

2. Spustenie aplikácie:
   java -jar target/lightbulb-game.jar

3. Spustenie pomocou Maven (ak máš podporu JavaFX pluginu):
   mvn javafx:run

4. Generovanie Javadoc dokumentácie:
   mvn javadoc:javadoc

   Výstupná dokumentácia sa nachádza v:
   target/site/apidocs/index.html


Poznámky:
---------
- Java 21+ je potrebná pre spustenie
- Logy sa ukladajú do priečinka data/logs
- Ikony a obrázky sú v priečinku lib/
