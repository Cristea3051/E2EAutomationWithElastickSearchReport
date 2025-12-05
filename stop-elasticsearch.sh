#!/bin/bash

echo "Stopping Elasticsearch and Kibana..."
echo "======================================"
echo ""

docker-compose down

echo ""
echo "Services stopped successfully!"
echo ""
echo "To remove data volumes (WARNING: This will delete all test results):"
echo "  docker-compose down -v"
echo ""
