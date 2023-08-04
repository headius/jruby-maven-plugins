package de.saumya.mojo.runit;

public class RunitMavenTestScriptFactory extends AbstractRunitMavenTestScriptFactory {

    @Override
    void getTestRunnerScript(StringBuilder builder) {
        builder.append("require 'fileutils'\n");
        builder.append("FileUtils.mkdir_p(File.dirname(REPORT_PATH))\n");
        builder.append("if defined?( Test::Unit::AutoRunner ) and Test::Unit::AutoRunner.respond_to?(:setup_option)\n");
        builder.append("  Test::Unit::AutoRunner.setup_option do |auto_runner, opts|\n");
        builder.append("    opts.on('--output-file=FILE', String, 'Outputs to file') do |file|\n");
        builder.append("      auto_runner.runner_options[:output] = Tee.open(file, 'w')\n");
        builder.append("    end\n");
        builder.append("  end\n");
        builder.append("  ARGV << \"--output-file=#{REPORT_PATH}\"\n");
        builder.append("else\n");
        builder.append("  begin\n");
        builder.append("    require 'minitest/autorun'\n");
        builder.append("    Minitest::Unit.output = Tee.open(REPORT_PATH, 'w') if Minitest::Unit.respond_to? :output=\n");
        builder.append("  rescue LoadError\n");
        builder.append("    require 'test/unit/ui/console/testrunner'\n");
        builder.append("    class Test::Unit::UI::Console::TestRunner\n");
        builder.append("      extend Test::Unit::UI::TestRunnerUtilities\n");
        builder.append("      alias :old_initialize :initialize\n");
        builder.append("      def initialize(suite, output_level=NORMAL)\n");
        builder.append("        old_initialize(suite, output_level, Tee.open(REPORT_PATH, 'w'))\n");
        builder.append("      end\n");
        builder.append("    end\n");
        builder.append("  end\n");
        builder.append("end\n");
    }

    @Override
    protected String getScriptName() {
        return "test-runner.rb";
    }

}
