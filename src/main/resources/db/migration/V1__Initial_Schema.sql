-- Create regions table
CREATE TABLE regions (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create cities table
CREATE TABLE cities (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    region_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cities_region FOREIGN KEY (region_id) REFERENCES regions(id)
);

-- Create citizens table
CREATE TABLE citizens (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    registered TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create campaigns table
CREATE TABLE campaigns (
    id INTEGER PRIMARY KEY,
    war_id INTEGER NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    invader_country VARCHAR(100) NOT NULL,
    defender_country VARCHAR(100) NOT NULL,
    region_id INTEGER NOT NULL,
    city_id INTEGER NOT NULL,
    war_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_campaigns_region FOREIGN KEY (region_id) REFERENCES regions(id),
    CONSTRAINT fk_campaigns_city FOREIGN KEY (city_id) REFERENCES cities(id)
);

-- Create rounds table
CREATE TABLE rounds (
    id INTEGER PRIMARY KEY,
    round SMALLINT NOT NULL,
    division VARCHAR(10) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    campaign_id INTEGER NOT NULL,
    invader_score INTEGER NOT NULL DEFAULT 0,
    defender_score INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rounds_campaign FOREIGN KEY (campaign_id) REFERENCES campaigns(id),
    CONSTRAINT uq_rounds_campaign_round_division UNIQUE (campaign_id, round, division)
);

-- Create combat_contributions table
CREATE TABLE combat_contributions (
    id BIGSERIAL PRIMARY KEY,
    citizen_id INTEGER NOT NULL,
    for_country VARCHAR(100) NOT NULL,
    damage BIGINT NOT NULL DEFAULT 0,
    round_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contributions_citizen FOREIGN KEY (citizen_id) REFERENCES citizens(id),
    CONSTRAINT fk_contributions_round FOREIGN KEY (round_id) REFERENCES rounds(id),
    CONSTRAINT uq_contributions_citizen_round UNIQUE (citizen_id, round_id)
);

-- Create indexes for better performance
CREATE INDEX idx_campaigns_war_id ON campaigns(war_id);
CREATE INDEX idx_campaigns_started ON campaigns(started_at);
CREATE INDEX idx_campaigns_invader_country ON campaigns(invader_country);
CREATE INDEX idx_campaigns_defender_country ON campaigns(defender_country);
CREATE INDEX idx_rounds_campaign_id ON rounds(campaign_id);
CREATE INDEX idx_rounds_round ON rounds(round);
CREATE INDEX idx_rounds_division ON rounds(division);
CREATE INDEX idx_rounds_started_at ON rounds(started_at);
CREATE INDEX idx_contributions_citizen_id ON combat_contributions(citizen_id);
CREATE INDEX idx_contributions_for_country ON combat_contributions(for_country);
CREATE INDEX idx_contributions_round_id ON combat_contributions(round_id);

-- Create a function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_regions_updated_at BEFORE UPDATE ON regions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_cities_updated_at BEFORE UPDATE ON cities
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_citizens_updated_at BEFORE UPDATE ON citizens
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_campaigns_updated_at BEFORE UPDATE ON campaigns
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_rounds_updated_at BEFORE UPDATE ON rounds
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_combat_contributions_updated_at BEFORE UPDATE ON combat_contributions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
