#!/bin/bash

echo "Starting Elasticsearch and Kibana with Docker..."
echo "=================================================="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Error: Docker is not running. Please start Docker and try again."
    exit 1
fi

# Start services
echo "Starting Elasticsearch and Kibana..."
docker-compose up -d

echo ""
echo "Waiting for services to be healthy..."
echo "This may take a minute or two..."
echo ""

# Wait for Elasticsearch
echo "Waiting for Elasticsearch..."
until curl -s http://localhost:9200/_cluster/health > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo ""
echo "✓ Elasticsearch is ready!"

# Wait for Kibana
echo "Waiting for Kibana..."
until curl -s http://localhost:5601/api/status > /dev/null 2>&1; do
    echo -n "."
    sleep 2
done
echo ""
echo "✓ Kibana is ready!"

echo ""
echo "=================================================="
echo "Services started successfully!"
echo ""
echo "Elasticsearch: http://localhost:9200"
echo "Kibana:        http://localhost:5601"
echo ""
echo "To view logs:"
echo "  docker-compose logs -f"
echo ""
echo "To stop services:"
echo "  ./stop-elasticsearch.sh"
echo "=================================================="
