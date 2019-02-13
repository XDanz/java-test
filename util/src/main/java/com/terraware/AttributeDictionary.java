package com.terraware;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class AttributeDictionary {
    private final CategoryId categoryId;
    private final Map<String, Set<MatchingAttribute>> dictionary;

    private AttributeDictionary(final CategoryId categoryId, final Map<String, Set<MatchingAttribute>> dictionary) {
        this.categoryId = Objects.requireNonNull(categoryId);
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public static AttributeDictionary of(final CategoryId categoryId) {
        return new AttributeDictionary(categoryId, new HashMap<>());
    }

    public CategoryId getCategoryId() {
        return categoryId;
    }

    public Map<String, Set<MatchingAttribute>> getDictionary() {
        return new HashMap<>(dictionary);
    }

    public Set<MatchingAttribute> getMatchesForValue(final String attributeValue) {
        return dictionary.getOrDefault(attributeValue, new HashSet<>());
    }

    public void addMatch(final String attributeValue, final AttributeId attributeId, final double probability) {
        final boolean attributeValueAlreadyExists = dictionary.putIfAbsent(attributeValue, SetUtil.of(new MatchingAttribute(attributeId, probability))) != null;
        if (attributeValueAlreadyExists) {
            dictionary.computeIfPresent(attributeValue, (key, oldMapping) -> {
                double totalProbability = probability;
                for (MatchingAttribute tMatchingAttribute : oldMapping) {
                    double tProbability = tMatchingAttribute.getProbability();
                    totalProbability = totalProbability + tProbability;
                }
                final double roundedTotalProbability = roundToTenDecimals(totalProbability);
                final HashSet<MatchingAttribute> newMapping = new HashSet<>(oldMapping);
                newMapping.add(new MatchingAttribute(attributeId, probability));
                return new HashSet<>(newMapping);
            });
        }
    }

    double roundToTenDecimals(final double probability){
        final BigDecimal bd = new BigDecimal(probability);
        return bd.setScale(10, RoundingMode.FLOOR).doubleValue();
    }

    public Optional<MatchingAttribute> getMostProbableMatchForAttributeValue(final String attributeValue) {
        return getDictionary().getOrDefault(attributeValue, new HashSet<>())
                .stream()
                .max(Comparator.comparingDouble(MatchingAttribute::getProbability));
    }

    public class MatchingAttribute {
        private final AttributeId attributeId;
        private final double probability;

        MatchingAttribute(final AttributeId attributeId, final double probability) {
            this.attributeId = Objects.requireNonNull(attributeId);
            this.probability = probability;
        }

        public AttributeId getAttribute() {
            return attributeId;
        }

        public double getProbability() {
            return probability;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final MatchingAttribute that = (MatchingAttribute) o;
            return Objects.equals(attributeId, that.attributeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(attributeId);
        }
    }

}
