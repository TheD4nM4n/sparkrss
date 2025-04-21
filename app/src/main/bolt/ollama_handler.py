from newspaper import Article, ArticleException
from ollama import chat, ChatResponse, pull, ResponseError
from os import environ


MODEL = environ.get('OLLAMA_MODEL', 'llama3.3')


class ModelNotFoundException(Exception):
    pass


class BoltOllamaHandler():

    def __init__(self):
        try:
            print("Pulling model...")
            pull(MODEL)
        except ResponseError:
            raise ModelNotFoundException("String in OLLAMA_MODEL is not available in the library. See https://ollama.com/library for models.")
    
    def get_summary(self, url: str) -> str:
        """
        Utilizes ollama to provide a summary of text at the given URL.

        Args:
            url (str): URL to the article

        Returns:
            str: a summary of the text at the given URL, or None if it fails.
        """

        article: Article = Article(url)
        article.download()

        # This covers malformed URLs, and checking if the URL is an article
        try:
            article.parse()
        except ArticleException as e:
            print(e)
            return None

        if article.text:
            response: ChatResponse = chat(model=MODEL, messages=[
                {
                    'role': 'user',
                    'content': f"Provide a quick summary of the following article text, without using bullet points. {article.text}"
                    }
                ]
            )

            return response['message']['content']
            
        else:
            # TODO: ERROR LOGGING
            return None

    def combined_summary(self, urls: list[str]) -> str:
        """
        Uses ollama to generate a single summary that
        encompasses all article URLs given.

        Args:
            urls (list[str]): list of article URLs

        Returns:
            str: summary of given article URLs
        """
        messages: list[dict] = []
        article_texts = ""

        for url in urls:
            article = Article(url)
            article.download()

            # Handling malformed URLs, non-article URLs
            try:
                article.parse()
            except ArticleException:
                # TODO: ERROR LOGGING
                return None
            
            article_texts += article.text

            messages.append({
                'role': 'user',
                'content': article.text
            })
        
        messages.append({
            'role': 'user',
            'content': f'Please write a summary that combines the content of the following articles, treating each as fact. {article_texts}'
        })

        return chat(model=MODEL, messages=messages, stream=True)

            

    
if __name__ == "__main__":
    ai_handler: BoltOllamaHandler = BoltOllamaHandler()

    print(ai_handler.get_summary('https://support.itesmedia.tv/flux-rss-disponibles'))

    print(ai_handler.combined_summary([
        'https://www.cnn.com/2025/02/03/politics/trump-musk-federal-government-what-matters/index.html',
        'https://www.nytimes.com/2025/02/03/us/politics/musk-federal-government.html',
        'https://www.huffpost.com/entry/democrats-trump-spending-elon-musk_n_67a1019fe4b099ec426d321b'
    ]))
