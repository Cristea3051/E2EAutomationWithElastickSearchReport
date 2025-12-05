# Elasticsearch Setup Guide with Docker

This guide will help you set up Elasticsearch and Kibana using Docker for test result reporting.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose installed (included with Docker Desktop)
- At least 4GB of available RAM

## Quick Start

### Option 1: Without Security (Development/Local Use)

**Linux/Mac:**
```bash
./start-elasticsearch.sh
```

**Windows:**
```cmd
start-elasticsearch.bat
```

This will start:
- **Elasticsearch** on http://localhost:9200
- **Kibana** on http://localhost:5601

### Option 2: With Security (Production Use)

```bash
docker-compose -f docker-compose-secure.yml up -d
```

Default credentials:
- Username: `elastic`
- Password: `changeme`

## Manual Setup Steps

### 1. Start Services

```bash
# Start in detached mode
docker-compose up -d

# Start with logs visible
docker-compose up
```

### 2. Verify Elasticsearch is Running

```bash
curl http://localhost:9200
```

Expected response:
```json
{
  "name" : "elasticsearch",
  "cluster_name" : "test-automation-cluster",
  "cluster_uuid" : "...",
  "version" : {
    "number" : "8.11.3",
    ...
  },
  "tagline" : "You Know, for Search"
}
```

### 3. Verify Kibana is Running

Open browser and navigate to: http://localhost:5601

## Configure Framework to Use Elasticsearch

Update `src/test/resources/config.properties`:

### For Unsecured Setup:
```properties
elasticsearch.url=http://localhost:9200
elasticsearch.index=test-results
elasticsearch.enabled=true
elasticsearch.username=
elasticsearch.password=
```

### For Secured Setup:
```properties
elasticsearch.url=http://localhost:9200
elasticsearch.index=test-results
elasticsearch.enabled=true
elasticsearch.username=elastic
elasticsearch.password=changeme
```

## Run Your Tests

Now run your tests and results will be automatically sent to Elasticsearch:

```bash
mvn clean test
```

## View Results in Kibana

### 1. Access Kibana

Open http://localhost:5601 in your browser

### 2. Create Index Pattern

1. Click on the menu (☰) → Management → Stack Management
2. Click on "Index Patterns" under Kibana
3. Click "Create index pattern"
4. Enter `test-results*` as the index pattern
5. Click "Next step"
6. Select `startTime` as the time field
7. Click "Create index pattern"

### 3. Discover Test Results

1. Click on the menu (☰) → Analytics → Discover
2. Select the `test-results*` index pattern
3. You'll see all your test results with filters and search

### 4. Create Visualizations

#### Test Status Pie Chart

1. Menu → Analytics → Visualize Library
2. Click "Create visualization"
3. Select "Pie"
4. Select `test-results*` index
5. Configure:
   - Slice by: Terms
   - Field: `status.keyword`
6. Click "Update"
7. Save the visualization

#### Test Execution Timeline

1. Create new visualization → "Line"
2. Select `test-results*` index
3. Configure:
   - X-axis: Date Histogram on `startTime`
   - Y-axis: Count
   - Split series: Terms on `status.keyword`
4. Save the visualization

#### Browser Distribution

1. Create new visualization → "Pie"
2. Select `test-results*` index
3. Configure:
   - Slice by: Terms on `browser.keyword`
4. Save the visualization

### 5. Create Dashboard

1. Menu → Analytics → Dashboard
2. Click "Create dashboard"
3. Click "Add from library"
4. Select the visualizations you created
5. Arrange and resize as needed
6. Save the dashboard as "Test Automation Dashboard"

## Docker Commands Reference

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

### Stop and Remove Data
```bash
docker-compose down -v
```

### View Logs
```bash
# All services
docker-compose logs -f

# Elasticsearch only
docker-compose logs -f elasticsearch

# Kibana only
docker-compose logs -f kibana
```

### Restart Services
```bash
docker-compose restart
```

### Check Service Status
```bash
docker-compose ps
```

### Access Elasticsearch Container
```bash
docker exec -it test-automation-elasticsearch bash
```

## Troubleshooting

### Issue: Elasticsearch won't start

**Error:** `max virtual memory areas vm.max_map_count [65530] is too low`

**Solution (Linux):**
```bash
sudo sysctl -w vm.max_map_count=262144

# Make it permanent
echo "vm.max_map_count=262144" | sudo tee -a /etc/sysctl.conf
```

**Solution (Windows/WSL2):**
```powershell
# In PowerShell as Administrator
wsl -d docker-desktop
sysctl -w vm.max_map_count=262144
exit
```

### Issue: Port already in use

**Error:** `Bind for 0.0.0.0:9200 failed: port is already allocated`

**Solution:**
```bash
# Find what's using port 9200
lsof -i :9200  # Linux/Mac
netstat -ano | findstr :9200  # Windows

# Either stop that service or change port in docker-compose.yml
ports:
  - "9201:9200"  # Use 9201 instead
```

### Issue: Kibana can't connect to Elasticsearch

**Check:**
1. Elasticsearch is running: `curl http://localhost:9200`
2. Check Kibana logs: `docker-compose logs kibana`
3. Restart Kibana: `docker-compose restart kibana`

### Issue: Out of memory

**Solution:** Increase Docker memory allocation

**Docker Desktop:**
1. Settings → Resources
2. Increase Memory to at least 4GB
3. Click "Apply & Restart"

## Query Test Results via API

### Get All Test Results
```bash
curl -X GET "http://localhost:9200/test-results/_search?pretty"
```

### Get Failed Tests Only
```bash
curl -X GET "http://localhost:9200/test-results/_search?pretty" \
  -H 'Content-Type: application/json' \
  -d '{
    "query": {
      "match": {
        "status": "FAILED"
      }
    }
  }'
```

### Get Tests by Browser
```bash
curl -X GET "http://localhost:9200/test-results/_search?pretty" \
  -H 'Content-Type: application/json' \
  -d '{
    "query": {
      "match": {
        "browser": "firefox"
      }
    }
  }'
```

### Get Test Execution Statistics
```bash
curl -X GET "http://localhost:9200/test-results/_search?pretty" \
  -H 'Content-Type: application/json' \
  -d '{
    "size": 0,
    "aggs": {
      "status_counts": {
        "terms": {
          "field": "status.keyword"
        }
      },
      "browser_counts": {
        "terms": {
          "field": "browser.keyword"
        }
      },
      "avg_duration": {
        "avg": {
          "field": "duration"
        }
      }
    }
  }'
```

## Delete All Test Results

**Warning:** This will permanently delete all test results!

```bash
curl -X DELETE "http://localhost:9200/test-results"
```

## Configuration Options

### docker-compose.yml Environment Variables

#### Elasticsearch

| Variable | Description | Default |
|----------|-------------|---------|
| `ES_JAVA_OPTS` | JVM memory settings | `-Xms512m -Xmx512m` |
| `xpack.security.enabled` | Enable security | `false` |
| `discovery.type` | Cluster discovery | `single-node` |

#### Kibana

| Variable | Description | Default |
|----------|-------------|---------|
| `ELASTICSEARCH_HOSTS` | ES connection URL | `http://elasticsearch:9200` |
| `SERVER_HOST` | Kibana bind address | `0.0.0.0` |

## Backup and Restore

### Create Snapshot Repository
```bash
curl -X PUT "http://localhost:9200/_snapshot/test_results_backup" \
  -H 'Content-Type: application/json' \
  -d '{
    "type": "fs",
    "settings": {
      "location": "/usr/share/elasticsearch/backups"
    }
  }'
```

### Create Snapshot
```bash
curl -X PUT "http://localhost:9200/_snapshot/test_results_backup/snapshot_1?wait_for_completion=true"
```

### Restore from Snapshot
```bash
curl -X POST "http://localhost:9200/_snapshot/test_results_backup/snapshot_1/_restore"
```

## Production Deployment

For production use:

1. Use `docker-compose-secure.yml`
2. Change default password
3. Enable HTTPS/SSL
4. Set up proper backup strategy
5. Configure resource limits
6. Use external volumes
7. Set up monitoring

## Next Steps

1. Start Elasticsearch: `./start-elasticsearch.sh`
2. Run your tests: `mvn clean test`
3. Open Kibana: http://localhost:5601
4. Create index pattern and visualizations
5. Build your test automation dashboard!

## Useful Links

- [Elasticsearch Documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- [Kibana Documentation](https://www.elastic.co/guide/en/kibana/current/index.html)
- [Docker Documentation](https://docs.docker.com/)
