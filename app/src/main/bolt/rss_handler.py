import feedparser

class BoltRSSHandler():

    @staticmethod
    def __init__(self):
        pass

    @staticmethod
    def get_rss_body(url: str):
        """
        Returns the main text from the given URL.

        Args:
            url (str): URL to fetch RSS data from.
        """

        result = feedparser.parse(url)

        return result.entries[0]

if __name__ == "__main__":

    print(BoltRSSHandler.get_rss_body('https://blog.danlew.net/rss/'))