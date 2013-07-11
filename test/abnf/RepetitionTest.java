package abnf;

import org.junit.Test;
import automata.NFA;
import automata.NFAState;
import automata.NFAStateFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/18/13
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class RepetitionTest {
    @Test
    public void testToNFA() throws Exception {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                Repetition repetition = parser.repetition();
                return repetition.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;
        NFA expected;
        NFAState[] s;

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit((byte)0x11, s[1]);
        expected = new NFA(s[0], s[1]);
        nfa = tester.test(AbnfParserFactory.newInstance("%x11"));
        Assertion.assertEquivalent(expected, nfa);

        s = NFAStateFactory.newInstances(2);
        s[0].addTransit((byte)0x11, s[1]);
        expected = new NFA(s[0], s[1]);
        expected.getStartState().printToDot();
        nfa = tester.test(AbnfParserFactory.newInstance("1%x11"));
        Assertion.assertEquivalent(expected, nfa);

        s = NFAStateFactory.newInstances(3);
        s[0].addTransit((byte)0x11, s[2]).addTransit((byte)0x11, s[1]);
        expected = new NFA(s[0], s[1]);
        expected.getStartState().printToDot();
        System.out.println("====================");
        nfa = tester.test(AbnfParserFactory.newInstance("2%x11"));
        Assertion.assertEquivalent(expected, nfa);

        s = NFAStateFactory.newInstances(3);
        s[0].addTransit(s[1]).addTransit((byte)0x11, s[1]);
        expected = new NFA(s[0], s[1]);
        nfa = tester.test(AbnfParserFactory.newInstance("*%x11"));
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("*ABC"));
        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(s[1]).addTransit("ABC", s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("*2ABC"));
        s = NFAStateFactory.newInstances(3);
        s[0]    .addTransit(s[1]);
        s[0]    .addTransit("ABC", s[2])
                .addTransit(s[1]);
        s[2]    .addTransit("ABC", s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("2*4ABC"));
        s = NFAStateFactory.newInstances(5);
        s[0]    .addTransit("ABC", s[2])
                .addTransit("ABC", s[3])
                .addTransit(s[1]);
        s[3]    .addTransit("ABC", s[4])
                .addTransit(s[1]);
        s[4]    .addTransit("ABC", s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("2*2ABC"));
        s = NFAStateFactory.newInstances(3);
        s[0]    .addTransit("ABC", s[2])
                .addTransit("ABC", s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("2*ABC"));
        s = NFAStateFactory.newInstances(3);
        s[0]    .addTransit("ABC", s[2])
                .addTransit("ABC", s[1])
                .addTransit("ABC", s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("*(A B)"));
        nfa.getStartState().printToDot();

    }
}
