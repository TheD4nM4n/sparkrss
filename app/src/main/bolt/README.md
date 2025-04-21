# Bolt: SparkRSS AI handler and API
This project consists of the backend for SparkRSS, an
AI-powered, open-source RSS reader designed for busy
people who want to stay up-to-date with the latest news
and new developments within their circles.

## Requirements

### Python

This project was built on Python 3.12. It should
work on any Python version that can install the
required packages listed in `requirements.txt`.

### Ollama
This project currently only interfaces with Ollama.
The model used for the server can be set using the 
`OLLAMA_MODEL` environment variable. The default is
currently `llama3.3`.

## Architecture

For privacy, Bolt does not store any data. All data
is stored on the SparkRSS client and will be not be
logged in any way when requests to Bolt are made. This
removes the need for accounts an user authentication.