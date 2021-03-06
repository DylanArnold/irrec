package ceedubs.irrec
package regex
package gen

import RegexMatchGen._

import cats.implicits._
import org.scalacheck.Gen

trait RegexCandidates[In, M] {
  def genMatchingStream[Out](r: Regex[In, M, Out]): Gen[Stream[In]]

  def genCandidateStream[Out](r: Regex[In, M, Out]): Gen[Stream[In]]
}

object RegexCandidates {
  trait GenInRegexCandidates[In, M] extends RegexCandidates[In, M] {
    def genIn: Gen[In]

    override def genCandidateStream[Out](r: Regex[In, M, Out]): Gen[Stream[In]] =
      Gen.oneOf(genMatchingStream(r), Gen.containerOf[Stream, In](genIn))
  }

  implicit val regexCCandidates: RegexCandidates[Char, Match[Char]] =
    new GenInRegexCandidates[Char, Match[Char]] {
      def genIn: Gen[Char] = CharRegexGen.genSupportedChars

      def genMatchingStream[Out](r: Regex[Char, Match[Char], Out]): Gen[Stream[Char]] =
        dietRegexMatchingStreamGen[Char, Out](CharRegexGen.supportedCharacters)
          .apply(r)
    }

  implicit val regexMByteCandidates: RegexCandidates[Byte, Match[Byte]] =
    new GenInRegexCandidates[Byte, Match[Byte]] {
      def genIn: Gen[Byte] = RegexGen.standardByteConfig.gen

      def genMatchingStream[Out](r: Regex[Byte, Match[Byte], Out]): Gen[Stream[Byte]] =
        regexMMatchingStreamGen[Byte](RegexMatchGen.byteMatchingGen)
          .apply(r)
    }

  implicit val regexMIntCandidates: RegexCandidates[Int, Match[Int]] =
    new GenInRegexCandidates[Int, Match[Int]] {
      def genIn: Gen[Int] = RegexGen.standardIntConfig.gen

      def genMatchingStream[Out](r: Regex[Int, Match[Int], Out]): Gen[Stream[Int]] =
        regexMMatchingStreamGen[Int](RegexMatchGen.intMatchingGen)
          .apply(r)
    }

  implicit val regexMLongCandidates: RegexCandidates[Long, Match[Long]] =
    new GenInRegexCandidates[Long, Match[Long]] {
      def genIn: Gen[Long] = RegexGen.standardLongConfig.gen

      def genMatchingStream[Out](r: Regex[Long, Match[Long], Out]): Gen[Stream[Long]] =
        regexMMatchingStreamGen[Long](RegexMatchGen.longMatchingGen)
          .apply(r)
    }
}
