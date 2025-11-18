# API Test Scripts

Ce dossier contient les scripts de test HTTP pour l'API MIMA.

## Structure

### Historiques du Personnel (7 contrôleurs)
1. **agent-grade-history.http** - Historique des grades/promotions
2. **agent-training-history.http** - Historique des formations
3. **agent-function-history.http** - Historique des fonctions
4. **agent-award-history.http** - Historique des décorations
5. **agent-service-position-history.http** - Historique des positions de service
6. **agent-other-position-history.http** - Historique des autres positions
7. **agent-company-history.http** - Historique des affectations d'entreprise

### Opérations Maritimes (4 contrôleurs)
1. **ship-arrival-departure.http** - Arrivées/départs de navires
2. **ship-provisioning.http** - Avitaillement de navires
3. **ship-incident.http** - Incidents maritimes
4. **sts-operation.http** - Opérations ship-to-ship

## Format des URLs - Changement Important ⚠️

Tous les endpoints qui utilisaient des query parameters pour les dates ont été **refactorés pour utiliser des path variables**.

### Ancien Format (❌ OBSOLÈTE)
```
GET /api/ship-arrivals-departures/list/arrivals?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```

### Nouveau Format (✅ ACTUEL)
```
GET /api/ship-arrivals-departures/list/arrivals/2024-01-01T00:00:00/2024-12-31T23:59:59
```

## Endpoints Modifiés

### ShipArrivalDepartureController
- `/list/arrivals/{startDate}/{endDate}` - Arrivées par période
- `/list/departures/{startDate}/{endDate}` - Départs par période

### ShipProvisioningController
- `/list/period/{startDate}/{endDate}` - Avitaillements par période

### ShipIncidentController
- `/list/period/{startDate}/{endDate}` - Incidents par période

### STSOperationController
- `/list/period/{startDate}/{endDate}` - Opérations STS par période

## Format des Dates

Les dates doivent être au format ISO 8601 : `YYYY-MM-DDTHH:mm:ss`

**Exemples :**
- `2024-01-01T00:00:00` - 1er janvier 2024 à minuit
- `2024-12-31T23:59:59` - 31 décembre 2024 à 23:59:59

## Utilisation

### IntelliJ IDEA / VS Code avec REST Client
1. Ouvrir le fichier `.http` souhaité
2. Remplacer les variables de configuration :
   - `AGENT_TRACKING_ID_HERE`
   - `SHIP_TRACKING_ID_HERE`
   - `RECORD_TRACKING_ID_HERE`
   - etc.
3. Cliquer sur "Run" à côté de la requête souhaitée

### Variables d'environnement
Chaque fichier définit ses propres variables au début :
```http
@baseUrl = http://localhost:8080/api/...
@trackingId = TRACKING_ID_HERE
```

## Exemples de Requêtes

### Créer un enregistrement
```http
POST {{baseUrl}}/create
Content-Type: application/json

{
  "field1": "value1",
  "field2": "value2"
}
```

### Récupérer par période (Path Variables)
```http
GET {{baseUrl}}/list/period/2024-01-01T00:00:00/2024-12-31T23:59:59
```

### Récupérer par ID (Path Variable)
```http
GET {{baseUrl}}/get/{{trackingId}}
```

## Notes Importantes

1. **Tous les endpoints utilisent maintenant des path variables** pour les dates au lieu de query parameters
2. Les dates dans les path variables doivent être correctement encodées dans l'URL
3. Le format ISO 8601 est requis : `YYYY-MM-DDTHH:mm:ss`
4. Assurez-vous que le serveur est en cours d'exécution sur `http://localhost:8080`

## Support

Pour toute question concernant l'API, consulter la documentation Swagger disponible à :
```
http://localhost:8080/swagger-ui.html
```
