export class ScoringEngine {
  calculateScore(error) {
    const sevScore = this.scoreSeverity(error.severity) * 0.30;
    const freqScore = this.scoreFrequency(error.occurrences) * 0.20;
    const trendScore = this.scoreTrend(error.trend) * 0.20;
    const domainScore = this.scoreDomain(error.domain) * 0.15;
    const impactScore = this.scoreImpact(error.errorType) * 0.15;

    let total = Math.round(sevScore + freqScore + trendScore + domainScore + impactScore);
    
    if (total > 100) total = 100;
    if (total < 0) total = 0;
    
    error.urgencyScore = total;
    return total;
  }

  scoreSeverity(sev) {
    switch(sev) {
      case 'CRITICAL': return 100;
      case 'HIGH': return 75;
      case 'MEDIUM': return 50;
      case 'LOW': return 25;
      case 'INFO': return 10;
      default: return 25;
    }
  }

  scoreFrequency(freq) {
    if (!freq) return 20;
    if (freq === 1) return 20;
    if (freq <= 5) return 40;
    if (freq <= 20) return 60;
    if (freq <= 100) return 80;
    return 100;
  }

  scoreTrend(trend) {
    switch(trend) {
      case 'REGRESSION': return 100;
      case 'NEW': return 70;
      case 'RECURRING': return 50;
      case 'RESOLVED': return 0;
      default: return 50;
    }
  }

  scoreDomain(domain) {
    switch(domain) {
      case 'BLE': return 90;
      case 'CORE': return 80;
      case 'AUTH': return 70;
      case 'UI': return 60;
      case 'DB': return 50;
      case 'CLOUD': return 40;
      case 'TELEMETRY': return 30;
      default: return 50;
    }
  }

  scoreImpact(type) {
    switch(type) {
      case 'CRASH': return 100;
      case 'TYPE_ERROR': return 70;
      case 'RUNTIME': return 70;
      case 'TEST_FAILURE': return 90;
      case 'PATTERN_VIOLATION': return 40;
      default: return 50;
    }
  }
}
