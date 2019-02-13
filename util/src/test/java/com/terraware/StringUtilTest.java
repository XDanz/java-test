package com.terraware;

import java.util.HashMap;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;


/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class StringUtilTest {

    @Test
    public void testWC() {
        System.out.println("WC = " + StringUtil.wordCount("manchester united is also known as red devil"));
    }

    @Test
    public void testValidate() {
        Assert.assertTrue(StringUtil.validate("allanballan", "banan"));
        Assert.assertTrue(StringUtil.validate("allanballan", "llll"));
    }

    @Test
    public void testReverseString() {
        String s = "Daniel";
        assertEquals("leinaD", StringUtil.reverseString(s));
    }

    @Test
    public void name() {
        ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<>();
        map.put("Daniel", "Terranova");
        map.put("Lena", "Andersson");
        map.put("Moa", "Andersson");
        map.put("Lea", "Andersson");

        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("entry = " + entry);
            if ("Daniel".equals(entry.getKey())) {
                System.out.println("(B)size = " + map.size());
                map.remove(entry.getKey());
                System.out.println("(A)size = " + map.size());
            }

        }
        System.out.println("(A2)size = " + map.size());
        System.out.println(" -- After -- ");
        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("entry = " + entry);
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {

            map.remove(entry.getKey());
            System.out.println("\tsize = " + map.size());

        }

    }

    @Test
    public void testMoa() {
        String namn = "Moa Andersson";

        System.out.println(namn);


    }

    @Test
    public void testOptional() {
        String s = tt("dag").orElse("");
    }

    private Optional<String> tt(String a) {
        if (a.equals("daniel"))
            return Optional.of("daniel");
        else
            return Optional.empty();
    }


    @Test
    public void sumOfAttributeNameProbabilitiesMax10() {

        final String attributeValue = "value";

        final AttributeDictionary dictionary = AttributeDictionary.of(CategoryId.of(12));

        Map<AttributeId, Integer> attributeObservations1 = new HashMap<>();
        attributeObservations1.put(AttributeId.of("1"), 1);
        attributeObservations1.put(AttributeId.of("2"), 324);
        attributeObservations1.put(AttributeId.of("3"), 24);
        attributeObservations1.put(AttributeId.of("4"), 995);
        attributeObservations1.put(AttributeId.of("5"), 152);
        attributeObservations1.put(AttributeId.of("6"), 96);
        attributeObservations1.put(AttributeId.of("7"), 1290);
        attributeObservations1.put(AttributeId.of("8"), 44);
        attributeObservations1.put(AttributeId.of("9"), 166);
        attributeObservations1.put(AttributeId.of("a"), 1408);

        Map<AttributeId, Integer> attributeObservations2 = new HashMap<>();
        attributeObservations2.put(AttributeId.of("b"), 1267);
        attributeObservations2.put(AttributeId.of("c"), 136);
        attributeObservations2.put(AttributeId.of("d"), 442);
        attributeObservations2.put(AttributeId.of("e"), 129);
        attributeObservations2.put(AttributeId.of("f"), 236);
        attributeObservations2.put(AttributeId.of("g"), 20);
        attributeObservations2.put(AttributeId.of("h"), 1303);
        attributeObservations2.put(AttributeId.of("i"), 1686);
        attributeObservations2.put(AttributeId.of("j"), 1445);
        attributeObservations2.put(AttributeId.of("k"), 1360);

        Map<AttributeId, Integer> attributeObservations3 = new HashMap<>();
        attributeObservations3.put(AttributeId.of("l"), 67);
        attributeObservations3.put(AttributeId.of("m"), 913);
        attributeObservations3.put(AttributeId.of("n"), 541);
        attributeObservations3.put(AttributeId.of("o"), 1297);
        attributeObservations3.put(AttributeId.of("p"), 1575);
        attributeObservations3.put(AttributeId.of("q"), 1036);


        HashMap<AttributeId, Integer> attributeAndCorrespondingNumberOfObservations = new HashMap<>();
        attributeAndCorrespondingNumberOfObservations.putAll(attributeObservations1);
        attributeAndCorrespondingNumberOfObservations.putAll(attributeObservations2);
        attributeAndCorrespondingNumberOfObservations.putAll(attributeObservations3);

        final int totalObservationsOfAttributeValue = attributeAndCorrespondingNumberOfObservations.values().stream().mapToInt(i -> i).sum();
        System.out.println("totalObservationsOfAttributeValue = " + totalObservationsOfAttributeValue);

        for (Map.Entry<AttributeId, Integer> entry : attributeAndCorrespondingNumberOfObservations.entrySet()) {
            AttributeId attributeId = entry.getKey();
            Integer observationCount = entry.getValue();
            dictionary.addMatch(attributeValue, attributeId, observationCount.doubleValue() / totalObservationsOfAttributeValue);
        }

        Map<String, Set<AttributeDictionary.MatchingAttribute>> tDictionary = dictionary.getDictionary();

        for (String tS : tDictionary.keySet()) {
            Set<AttributeDictionary.MatchingAttribute> tMatchingAttributes = tDictionary.get(tS);
            double sum = 0d;
            for (AttributeDictionary.MatchingAttribute tMatchingAttribute : tMatchingAttributes) {
                sum += tMatchingAttribute.getProbability();
            }
            System.out.println("sum = " + sum);
        }

        dictionary.getMostProbableMatchForAttributeValue("value");


    }
}
