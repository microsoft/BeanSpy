<?xml version="1.0" encoding="utf-8"?>
<!-- Copyright (c) Microsoft Corporation                                               -->
<!--                                                                                 -->
<!-- Licensed under the Apache License, Version 2.0 (the "License"); you may not use -->
<!-- this file except in compliance with the License. You may obtain a copy of the   -->
<!-- License at http://www.apache.org/licenses/LICENSE-2.0.                          -->
<!--                                                                                 -->
<!-- THIS CODE IS PROVIDED *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS           -->
<!-- OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION            -->
<!-- ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,-->
<!-- MERCHANTABLITY OR NON-INFRINGEMENT.                                             -->
<!--                                                                                 -->
<!-- See the Apache Version 2.0 License for specific language governing              -->
<!-- permissions and limitations under the License.                                  -->
<!--                                                                                 -->
<!--                                                                                 -->
<!-- This XSLT transform will make the output of the JUNIT testruns more palpable.   -->
<!-- The default behavior of the ANT JUNIT task is to create a small XML             -->
<!-- file-per-test-class.  These are summed into a single file, but the data         -->
<!-- desired for the nightly build emails is available, but uncorrelated.            -->
<!-- Generally, these XML files are filled with a lot of system/environment data     -->
<!-- that is uninteresting to the results of the test output.  All that is really    -->
<!-- desired is a simple summary of “there were X number of tests and Y did not      -->
<!-- work”.                                                                          -->
<!-- This stylesheet counts the number of tests, as well as the number of errors     -->
<!-- and failures.  An error would be hitting an Assert.Fail() or some other         -->
<!-- assert that did not match the expected output.  An Error is hitting an          -->
<!-- uncaught exception.  The FailuresTotal is a sum of these two numbers.           -->
<!-- To that end, this stylesheet gives the desired data in a format like:           -->
<!--                                                                                 -->
<!-- <?xml version="1.0" encoding="utf-8" standalone="yes" ?>                        -->
<!-- <Statistics>                                                                    -->
<!--  <Tests>37</Tests>                                                              -->
<!--  <FailuresTotal>2</FailuresTotal>                                               -->
<!--  <Errors>1</Errors>                                                             -->
<!--  <Failures>1</Failures>                                                         -->
<!-- </Statistics>                                                                   -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
	exclude-result-prefixes="msxsl">
  <xsl:output method="xml" indent="yes" encoding="utf-8"
		standalone="yes" />

  <xsl:template match="/">
    <xsl:variable name="failures" select='sum(/testsuites/testsuite/@failures)' />
    <xsl:variable name="errors" select='sum(/testsuites/testsuite/@errors)' />
    <TestRun>
      <Statistics>
        <Tests>
          <xsl:value-of select='sum(/testsuites/testsuite/@tests)' />
        </Tests>
        <FailuresTotal>
          <xsl:value-of select="$failures + $errors" />
        </FailuresTotal>
        <Errors>
          <xsl:value-of select='$errors' />
        </Errors>
        <Failures>
          <xsl:value-of select="$failures" />
        </Failures>
      </Statistics>
    </TestRun>
  </xsl:template>
</xsl:stylesheet>
