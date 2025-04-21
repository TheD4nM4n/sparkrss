from flask import Flask, request
from ollama_handler import BoltOllamaHandler

app = Flask(__name__)
ollama = BoltOllamaHandler()


@app.route('/get-summary', methods=['GET'])
def get_summary_route():

    url = request.args.get('url')
    return ollama.get_summary(url)


@app.route('/combined-summary', methods=['POST'])
def combined_summary_route():
    """
    Route that returns a summary of the article URLs given.
    400 most likely means that the URLs weren't a part of the request.

    Returns:
        str: summary of given articles
    """

    # No URLs provided
    try:
        urls = request.json['urls']
    except KeyError:
        return 'URLs not provided.', 400
    
    return ollama.combined_summary(urls)


if __name__ == "__main__":
    app.run(host='0.0.0.0')
