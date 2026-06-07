## Project Notes

### OpenWeb UI

- used for running local models using an Interface
- Command for pulling and setup using docker models:


  ```docker run -d -p 3003:8080 --add-host=host.docker.internal:host-gateway -v open-webui:/app/backend/data -e OPENAI_API_BASEURL=http://model-runner.docker.internal/engines/llama.cpp/v1 --name open-webui --restart always ghcr.io/open-webui/open-webui:main```
  
---
