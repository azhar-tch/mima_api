-- Migration pour créer la table des alertes de violation de règles de gestion

CREATE TABLE IF NOT EXISTS rule_violation_alerts (
    id BIGSERIAL PRIMARY KEY,
    tracking_id UUID NOT NULL UNIQUE,
    rule_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    agent_id BIGINT NOT NULL,
    mission_tracking_id UUID,
    details TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    can_be_overridden BOOLEAN NOT NULL DEFAULT FALSE,
    resolved_date TIMESTAMP,
    resolved_by_id BIGINT,
    resolution_comment TEXT,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    CONSTRAINT fk_alert_agent FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE CASCADE,
    CONSTRAINT fk_alert_resolved_by FOREIGN KEY (resolved_by_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Créer des index pour améliorer les performances des requêtes
CREATE INDEX idx_rule_violation_alerts_tracking_id ON rule_violation_alerts(tracking_id);
CREATE INDEX idx_rule_violation_alerts_agent_id ON rule_violation_alerts(agent_id);
CREATE INDEX idx_rule_violation_alerts_status ON rule_violation_alerts(status);
CREATE INDEX idx_rule_violation_alerts_rule_type ON rule_violation_alerts(rule_type);
CREATE INDEX idx_rule_violation_alerts_severity ON rule_violation_alerts(severity);
CREATE INDEX idx_rule_violation_alerts_create_date ON rule_violation_alerts(create_date);

-- Commentaires sur la table et les colonnes
COMMENT ON TABLE rule_violation_alerts IS 'Table stockant les alertes de violation des règles de gestion du personnel';
COMMENT ON COLUMN rule_violation_alerts.rule_type IS 'Type de règle violée (DOUBLE_ASSIGNMENT, INSUFFICIENT_REST, WEEKLY_HOURS_EXCEEDED, UNJUSTIFIED_ABSENCE, EQUITY_DISTRIBUTION)';
COMMENT ON COLUMN rule_violation_alerts.severity IS 'Niveau de sévérité (INFO, WARNING, ERROR, CRITICAL)';
COMMENT ON COLUMN rule_violation_alerts.status IS 'Statut de l''alerte (ACTIVE, RESOLVED, OVERRIDDEN, DISMISSED)';
COMMENT ON COLUMN rule_violation_alerts.can_be_overridden IS 'Indique si l''alerte peut être annulée par un administrateur';
