FROM debian:bookworm-slim

ARG OLLAMA_VERSION=0.12.9
ARG OLLAMA_URL=https://github.com/ollama/ollama/releases/download/v${OLLAMA_VERSION}/ollama-linux-amd64.tgz

RUN apt-get update && apt-get install -y \
    curl \
    ca-certificates \
    tar \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

COPY ollama-linux-amd64.tgz /tmp/ollama.tgz

RUN cd /tmp && \
    tar -xzf ollama.tgz && \
    find . -name "ollama" -type f -exec mv {} /usr/local/bin/ \; && \
    chmod +x /usr/local/bin/ollama && \
    rm /tmp/ollama.tgz && \
    mkdir -p /root/.ollama && \
    ollama --version

EXPOSE 11434

CMD ["ollama", "serve"]