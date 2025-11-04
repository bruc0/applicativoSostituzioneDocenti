#!/usr/bin/env python3
import re

# Lista di cognomi italiani realistici (più di 73 per sicurezza)
cognomi_italiani = [
    "Rossi", "Russo", "Ferrari", "Esposito", "Bianchi", "Romano", "Colombo", "Ricci",
    "Marino", "Greco", "Bruno", "Gallo", "Conti", "De Luca", "Costa", "Giordano",
    "Mancini", "Rizzo", "Lombardi", "Moretti", "Barbieri", "Fontana", "Santoro", "Mariani",
    "Rinaldi", "Caruso", "Ferrara", "Galli", "Martini", "Leone", "Longo", "Gentile",
    "Martinelli", "Vitale", "Lombardo", "Serra", "Coppola", "Morelli", "Marchetti", "Parisi",
    "Bernardi", "D'Angelo", "Rossi", "Ferraro", "Silvestri", "Ferretti", "Valentini", "Messina",
    "Sala", "De Santis", "Gatti", "Pellegrini", "Palumbo", "Sanna", "Farina", "Riva",
    "Molinari", "Zanetti", "Montanari", "Bellini", "Basile", "Ferri", "Neri", "Ruggiero",
    "Lombardi", "Bianco", "Marini", "Grassi", "Pellegrino", "Colletti", "Orlando", "Damico",
    "Guerra", "Benedetti", "Barone", "Cattaneo", "Sartori", "Ferroni", "Cortese", "Mancuso",
    "Amato", "Parisi", "Rinaldi", "Sorrentino", "D'Amico", "Giuliano", "Pagano", "Rizzo",
    "Vitali", "Mazzola", "Piras", "Grasso", "Lombardo", "Donati", "Cavallo", "Fabbri",
    "Testa", "Grimaldi", "Bianchi", "Sala", "De Angelis", "Pugliese", "Viviani", "Santini",
    "Cipriani", "Mancini", "Rizzi", "Gentile", "Barberio", "Esposito", "Ferrara", "Mariano"
]

# Leggi il file CSV
with open('/home/bruc0/Scrivania/GestioneSostituzioni/ApplicativoGestioneSostituzioni/src/OrarioDocenti_Fake.csv', 'r', encoding='utf-8') as f:
    content = f.read()

# Trova tutti i cognomi fittizi unici (inclusi quelli con numeri finali)
fake_surnames = list(set(re.findall(r'Cognome\d+', content)))
fake_surnames.sort(key=lambda x: int(x[7:]))  # Ordina per numero

# Trova anche eventuali cognomi parzialmente sostituiti (es. "Romano8")
partial_surnames = list(set(re.findall(r'[A-Z][a-z]+\d+', content)))
for surname in partial_surnames:
    if surname not in fake_surnames and any(name in surname for name in cognomi_italiani):
        fake_surnames.append(surname)

print(f"Trovati {len(fake_surnames)} cognomi fittizi unici da sostituire")

# Crea un dizionario di mapping assicurandosi che non ci siano duplicati
surname_mapping = {}
used_surnames = set()

for i, fake_surname in enumerate(fake_surnames):
    # Trova un cognome non ancora usato
    candidate_index = i % len(cognomi_italiani)
    base_surname = cognomi_italiani[candidate_index]

    # Se il cognome base è già stato usato, cerca il prossimo disponibile
    while base_surname in used_surnames:
        candidate_index = (candidate_index + 1) % len(cognomi_italiani)
        base_surname = cognomi_italiani[candidate_index]

    used_surnames.add(base_surname)
    surname_mapping[fake_surname] = base_surname

# Sostituisci tutti i cognomi fittizi
new_content = content
for fake_surname, real_surname in surname_mapping.items():
    new_content = new_content.replace(fake_surname, real_surname)

# Scrivi il file modificato
with open('/home/bruc0/Scrivania/GestioneSostituzioni/ApplicativoGestioneSostituzioni/src/OrarioDocenti_Fake.csv', 'w', encoding='utf-8') as f:
    f.write(new_content)

print("Sostituzione completata!")
print("Mapping dei cognomi:")
for fake, real in surname_mapping.items():
    print(f"  {fake} -> {real}")