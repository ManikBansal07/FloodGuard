import numpy as np
import logging

logger = logging.getLogger(__name__)

class FinancialRiskCalculator:
    def __init__(self):
        # Risk factors for different flood severity levels
        self.severity_factors = {
            'low': 0.1,
            'medium': 0.3,
            'high': 0.6,
            'extreme': 0.9
        }
        
        # Location risk multipliers (example values)
        self.location_risk_multipliers = {
            'flood_plain': 1.5,
            'coastal': 1.3,
            'urban': 1.2,
            'rural': 1.0
        }

    def calculate(self, location, property_value, flood_severity):
        try:
            # Get base risk factor for flood severity
            severity_factor = self.severity_factors.get(flood_severity.lower(), 0.1)
            
            # Get location risk multiplier
            location_type = location.get('type', 'rural')
            location_multiplier = self.location_risk_multipliers.get(location_type, 1.0)
            
            # Calculate base damage estimate
            base_damage = property_value * severity_factor * location_multiplier
            
            # Calculate business interruption cost (if applicable)
            business_interruption = self._calculate_business_interruption(
                property_value,
                severity_factor,
                location
            )
            
            # Calculate insurance payout estimate
            insurance_payout = self._calculate_insurance_payout(
                base_damage,
                location,
                flood_severity
            )
            
            return {
                'estimated_damage': float(base_damage),
                'business_interruption': float(business_interruption),
                'insurance_payout': float(insurance_payout),
                'risk_level': self._determine_risk_level(severity_factor * location_multiplier),
                'timestamp': np.datetime64('now').astype(str)
            }
        except Exception as e:
            logger.error(f"Error in financial risk calculation: {str(e)}")
            raise

    def _calculate_business_interruption(self, property_value, severity_factor, location):
        # Simple business interruption calculation
        # This is a placeholder - implement more sophisticated calculation
        if location.get('is_business', False):
            return property_value * severity_factor * 0.3
        return 0.0

    def _calculate_insurance_payout(self, base_damage, location, flood_severity):
        # Simple insurance payout calculation
        # This is a placeholder - implement more sophisticated calculation
        coverage_factor = 0.8  # Example coverage factor
        deductible = base_damage * 0.1  # Example deductible
        
        return max(0, base_damage * coverage_factor - deductible)

    def _determine_risk_level(self, risk_score):
        if risk_score >= 0.7:
            return 'extreme'
        elif risk_score >= 0.5:
            return 'high'
        elif risk_score >= 0.3:
            return 'medium'
        else:
            return 'low' 