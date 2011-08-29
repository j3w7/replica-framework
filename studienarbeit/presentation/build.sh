#!/bin/bash
pdflatex --file-line-error-style ReplicaFramework_beamer-english.tex
pdflatex --file-line-error-style ReplicaFramework_beamer-english.tex
bibtex ReplicaFramework_beamer-english.aux
pdflatex --file-line-error-style ReplicaFramework_beamer-english.tex
pdflatex --file-line-error-style ReplicaFramework_beamer-english.tex
