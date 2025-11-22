# Implémentation des Règles de Gestion

Ce document décrit l'implémentation du système de règles de gestion pour la gestion du personnel maritime.

## Vue d'ensemble

Le système implémente 6 règles principales de gestion conformément au cahier des charges :

1. **Aucune double affectation** : Un agent ne peut pas être affecté à deux postes simultanés
2. **Repos minimal obligatoire** : Temps de repos minimal entre deux missions
3. **Durée maximale hebdomadaire** : Limite du nombre d'heures de service par semaine
4. **Signalement automatique des absences non justifiées**
5. **Équité de répartition** : Distribution équitable des missions entre agents
6. **Système de notifications** : Notifications automatiques en cas de violation

## Architecture

### Entités

#### ManagementRules
Stocke les paramètres des règles de gestion :
- `preventDoubleAssignment` : Active/désactive la règle de double affectation
- `minRestHours` : Nombre d'heures de repos minimal entre missions
- `maxWeeklyHours` : Nombre maximal d'heures hebdomadaires
- `autoReportUnjustifiedAbsences` : Active/désactive le signalement automatique
- `enforceEquityDistribution` : Active/désactive la vérification d'équité

### Services

#### ManagementRulesValidationService
Service principal de validation des règles :

```java
// Valider toutes les règles pour une affectation
List<RuleViolation> validateAllRules(
    UUID agentTrackingId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    UUID currentMissionTrackingId
);

// Vérifier la règle de double affectation
List<RuleViolation> validateNoDoubleAssignment(...);

// Vérifier le repos minimal
List<RuleViolation> validateMinimumRest(...);

// Vérifier la durée hebdomadaire
List<RuleViolation> validateWeeklyMaxHours(...);

// Détecter les absences non justifiées
List<RuleViolation> detectUnjustifiedAbsences();

// Vérifier l'équité de répartition
List<RuleViolation> validateEquityDistribution(...);
```

### Système de Notifications

Les violations des règles génèrent des notifications via le système de notifications existant (identique aux notifications de missions).

Types de notifications :
- `absences_non_justifiees` : Absences sans justification
- `equite_repartition_hebdo` : Rapport hebdomadaire d'équité
- `equite_repartition_mensuel` : Rapport mensuel d'équité

### API Endpoints

#### Validation des règles
```
POST /api/management-rules/validate/assignment
  - Params: agentTrackingId, startDate, endDate, currentMissionTrackingId
  - Retourne: Liste des violations et si l'affectation peut être faite

GET /api/management-rules/validate/double-assignment
  - Params: agentTrackingId, startDate, endDate, currentMissionTrackingId
  - Retourne: Violations de double affectation

GET /api/management-rules/validate/minimum-rest
  - Params: agentTrackingId, newMissionStartDate
  - Retourne: Violations de repos minimal

GET /api/management-rules/validate/weekly-hours
  - Params: agentTrackingId, weekStartDate, additionalHours
  - Retourne: Violations de durée hebdomadaire

GET /api/management-rules/validate/unjustified-absences
  - Retourne: Liste des absences non justifiées

GET /api/management-rules/validate/equity-distribution
  - Params: periodStart, periodEnd
  - Retourne: Déséquilibres de répartition

GET /api/management-rules/validate/worked-hours
  - Params: agentTrackingId, startDate, endDate
  - Retourne: Heures travaillées calculées
```

### Tâches planifiées

Le système exécute automatiquement :

1. **Vérification des absences non justifiées** (Toutes les heures)
   - Détecte les absences sans justification
   - Envoie des notifications automatiquement

2. **Vérification hebdomadaire de l'équité** (Lundi 8h)
   - Analyse la répartition de la semaine précédente
   - Notifie en cas de déséquilibre significatif

3. **Vérification mensuelle de l'équité** (1er du mois 9h)
   - Analyse la répartition du mois précédent
   - Génère un rapport de déséquilibre

## Utilisation

### 1. Configuration des règles

Créer ou mettre à jour les règles de gestion :

```bash
POST /api/management-rules/create
{
  "ruleName": "Règles standard 2024",
  "preventDoubleAssignment": true,
  "minRestHours": 12,
  "maxWeeklyHours": 48,
  "autoReportUnjustifiedAbsences": true,
  "enforceEquityDistribution": true,
  "description": "Règles de gestion du personnel maritime",
  "effectiveDate": "2024-01-01T00:00:00"
}
```

### 2. Validation avant affectation

Avant d'affecter un agent à une mission, valider :

```bash
POST /api/management-rules/validate/assignment?agentTrackingId=xxx&startDate=2024-01-01T08:00:00&endDate=2024-01-10T18:00:00

Response:
{
  "error": false,
  "message": "2 violation(s) détectée(s)",
  "data": {
    "violations": [
      {
        "ruleType": "INSUFFICIENT_REST",
        "severity": "ERROR",
        "message": "Repos insuffisant: Jean Dupont...",
        "canBeOverridden": true
      }
    ],
    "canProceed": false,
    "warningsOnly": false
  }
}
```

## Niveaux de sévérité

- **INFO** : Information, pas de blocage
- **WARNING** : Avertissement, peut procéder avec prudence
- **ERROR** : Erreur, ne devrait pas procéder sans validation
- **CRITICAL** : Critique, ne peut pas procéder (ex: double affectation)

## Workflow d'affectation recommandé

1. **Pré-validation**
   ```
   POST /api/management-rules/validate/assignment
   ```

2. **Analyse des violations**
   - Si violations CRITICAL : Bloquer l'affectation
   - Si violations ERROR : Demander confirmation admin
   - Si violations WARNING : Afficher avertissement
   - Si pas de violations : Procéder

3. **Création de l'affectation**
   - Si validée, créer l'affectation
   - Les notifications seront envoyées automatiquement par le scheduler

4. **Notifications**
   - Les utilisateurs reçoivent des notifications comme pour les missions
   - Visualisation via le système de notifications existant

## Notifications

Le système envoie automatiquement des notifications :
- Aux utilisateurs lors de la détection de violations
- Avec un niveau de sévérité visuel (🔴 🟠 🟡 ℹ️)
- Via le système de notifications existant (identique aux missions)

## Monitoring

Endpoints de monitoring :
- Validation des affectations
- Heures travaillées par agent
- Rapport d'équité de répartition

## Évolutions futures

- Dashboard de visualisation des violations
- Rapports automatiques mensuels
- Configuration des règles par département
- Historique des violations par agent
- Prédiction des conflits d'affectation
