#!/bin/bash
pdflatex --file-line-error-style SeminarPaper.tex
pdflatex --file-line-error-style SeminarPaper.tex
bibtex SeminarPaper.aux
makeindex SeminarPaper.idx
latex-indexbuild --words-list=indexwords
pdflatex --file-line-error-style SeminarPaper.tex
pdflatex --file-line-error-style SeminarPaper.tex
./clean.sh &> /dev/null
