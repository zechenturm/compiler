package ch.ntb.inf.deep.comp.targettest.casts;

import ch.ntb.inf.junitTarget.MaxErrors;
import ch.ntb.inf.junitTarget.Suite;

@Suite({ ByteCastTest.class, ShortCastTest.class, CharCastTest.class, IntCastTest.class, LongCastTest.class, FloatCastTest.class, DoubleCastTest.class  })
@MaxErrors(100)
public class CastSuite {

}