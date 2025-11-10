# Esportatore PDF per Orario.csv

## Descrizione
La classe `EsportatorePDF.java` permette di esportare il file `Orario.csv` in formato PDF con una formattazione professionale e leggibile.

## Requisiti

### Libreria iText
Per utilizzare questa classe è necessario aggiungere la libreria **iText** al progetto. iText è una libreria Java per la creazione e manipolazione di documenti PDF.

### Installazione della libreria iText

#### Opzione 1: Download manuale (per IntelliJ IDEA)

1. Scarica la libreria iText dal sito ufficiale o da Maven Central:
   - iText 5.x (versione consigliata per questo progetto): https://github.com/itext/itextpdf/releases
   - Oppure scarica direttamente il JAR da Maven Central:
     - `itextpdf-5.5.13.2.jar` (o versione più recente della serie 5.x)

2. In IntelliJ IDEA:
   - Vai su `File` → `Project Structure` (oppure premi `Ctrl+Alt+Shift+S`)
   - Seleziona `Modules` → `Dependencies`
   - Clicca sul `+` e seleziona `JARs or directories`
   - Naviga fino al file JAR scaricato e selezionalo
   - Clicca `Apply` e poi `OK`

#### Opzione 2: Usando Maven (se il progetto usa Maven)

Aggiungi questa dipendenza al file `pom.xml`:

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.13.2</version>
</dependency>
```

#### Opzione 3: Usando Gradle (se il progetto usa Gradle)

Aggiungi questa dipendenza al file `build.gradle`:

```gradle
dependencies {
    implementation 'com.itextpdf:itextpdf:5.5.13.2'
}
```

## Utilizzo

### Esempio 1: Esportazione semplice
```java
// Esporta Orario.csv in Orario.pdf (stesso nome, estensione .pdf)
EsportatorePDF.esporta("Orario.csv");
```

### Esempio 2: Esportazione con percorso personalizzato
```java
// Specifica il percorso di output
EsportatorePDF.esporta("Orario.csv", "output/OrarioCompleto.pdf");
```

### Esempio 3: Utilizzo con istanza della classe
```java
EsportatorePDF esportatore = new EsportatorePDF("Orario.csv", "Orario.pdf");

if (esportatore.esportaPDF()) {
    System.out.println("PDF generato con successo!");
} else {
    System.out.println("Errore durante l'esportazione.");
}
```

### Esempio 4: Integrazione con il Database esistente
```java
// Nel tuo codice esistente, dopo aver scritto il file CSV
Database db = new Database(new File("OrarioDocenti_Fake.csv"));
db.letturaFile();
db.scriviFile(); // Scrive Orario.csv

// Esporta in PDF
EsportatorePDF.esporta("Orario.csv");
```

## Caratteristiche

- **Orientamento orizzontale**: Il PDF viene generato in formato landscape (orizzontale) per meglio visualizzare le tabelle larghe
- **Formattazione professionale**:
  - Intestazione con sfondo blu
  - Righe alternate con sfondo grigio chiaro per migliore leggibilità
  - Testo centrato nelle celle
  - Larghezze colonne ottimizzate per il formato dell'orario
- **Titolo e footer**: Include un titolo "Orario Scolastico" e la data di generazione
- **Gestione errori**: Gestisce correttamente file mancanti o errori di I/O

## Formato CSV supportato

La classe è ottimizzata per il formato CSV dell'orario con le seguenti colonne:
- NUMERO
- DURATA
- MAT_NOME
- DOC_COGN
- CLASSE
- CO-DOC.
- GIORNO
- O.INIZIO

## Note

- Il file CSV deve esistere e contenere almeno l'intestazione
- Il PDF viene generato con encoding UTF-8 per supportare caratteri speciali
- Se il file di output esiste già, verrà sovrascritto
- La classe gestisce automaticamente la creazione delle directory necessarie per il file di output

## Risoluzione problemi

### Errore: "Cannot resolve symbol 'itextpdf'"
Significa che la libreria iText non è stata aggiunta al progetto. Segui le istruzioni di installazione sopra.

### Il PDF è vuoto
Verifica che il file CSV contenga dati e non sia vuoto.

### Errore di permessi
Assicurati di avere i permessi di scrittura nella directory di output.

## Autore
Classe creata per il progetto Gestione Sostituzioni
