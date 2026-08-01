import http.server
import socketserver
from pathlib import Path

PORT = 8000
ROOT = Path(__file__).resolve().parent

class Handler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(ROOT), **kwargs)

# Use a port that is less likely to be blocked by firewall restrictions.
with socketserver.TCPServer(("0.0.0.0", PORT), Handler) as httpd:
    print(f"Serving calculator at http://localhost:{PORT}")
    httpd.serve_forever()
