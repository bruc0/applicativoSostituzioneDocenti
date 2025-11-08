#!/usr/bin/env python3
import csv

input_file = '/home/bruc0/Scrivania/GestioneSostituzioni/ApplicativoGestioneSostituzioni/OrarioDocenti_Fake.csv'
output_file = '/home/bruc0/Scrivania/GestioneSostituzioni/ApplicativoGestioneSostituzioni/OrarioDocenti_Fake.csv'

# Leggi il file originale
with open(input_file, 'r', encoding='utf-8') as infile:
    # Usa csv.reader per gestire correttamente le virgolette
    reader = csv.reader(infile, delimiter=';', quotechar='"')
    rows = list(reader)

# Scrivi il file con il nuovo formato
with open(output_file, 'w', encoding='utf-8', newline='') as outfile:
    writer = csv.writer(outfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_NONE, escapechar='\\')

    for row in rows:
        # Per ogni campo, sostituisci "; " con ";"
        cleaned_row = [field.replace('; ', ';') for field in row]
        writer.writerow(cleaned_row)

print("Conversione completata!")
print(f"Righe processate: {len(rows)}")
