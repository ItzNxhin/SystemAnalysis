## System Analysis - Workshop 1

Problem:  https://github.com/EngAndres/ud-public/blob/main/courses/systems-analysis/workshops/Workshop_1.pdf

## Development

To solve the problem, we create 6 classes:

- `App`: Class to maintain the main of the program
- `GeneratorBD`: Class to create artificial database, generate secuences and save them in a file (For default in App.java, 'database.txt') 
- `Reader`: Class to read database..
- `Motif`: Class to iterate over data (Arraylist from database, given by reader) to generate all combinantion in each secuence and return the motif.
- `EntropyMotif`: Class similary to Motif, but introducing Shannon Concept (3rd step from the problem).

## Folder structure

- `bin`: Compiled files output (.class).
- `.vscode`: Project and vscode for this project config.
- `data`: Here is the database, results and report.
- `src`: Source folder (Development).
- `lib` : Dependencies .
