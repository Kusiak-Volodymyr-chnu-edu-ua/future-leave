package com.voltor.futureleave.service.djl;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import ai.djl.engine.Engine;


public class Arguments {

    protected int epoch;
    protected int batchSize;
    protected int maxGpus;
    protected boolean isSymbolic;
    protected boolean preTrained;
    protected String outputDir;
    protected long limit;
    protected String modelDir;
    protected void initialize() {
        epoch = 2;
        maxGpus = Engine.getInstance().getGpuCount();
        outputDir = "build/model";
        limit = Long.MAX_VALUE;
        modelDir = null;
    }
 
    public Options getOptions() {
        Options options = new Options();
        options.addOption(
                Option.builder("h").longOpt("help").hasArg(false).desc("Print this help.").build());
        options.addOption(
                Option.builder("e")
                        .longOpt("epoch")
                        .hasArg()
                        .argName("EPOCH")
                        .desc("Numbers of epochs user would like to run")
                        .build());
        options.addOption(
                Option.builder("b")
                        .longOpt("batch-size")
                        .hasArg()
                        .argName("BATCH-SIZE")
                        .desc("The batch size of the training data.")
                        .build());
        options.addOption(
                Option.builder("g")
                        .longOpt("max-gpus")
                        .hasArg()
                        .argName("MAXGPUS")
                        .desc("Max number of GPUs to use for training")
                        .build());
        options.addOption(
                Option.builder("s")
                        .longOpt("symbolic-model")
                        .argName("SYMBOLIC")
                        .desc("Use symbolic model, use imperative model if false")
                        .build());
        options.addOption(
                Option.builder("p")
                        .longOpt("pre-trained")
                        .argName("PRE-TRAINED")
                        .desc("Use pre-trained weights")
                        .build());
        options.addOption(
                Option.builder("o")
                        .longOpt("output-dir")
                        .hasArg()
                        .argName("OUTPUT-DIR")
                        .desc("Use output to determine directory to save your model parameters")
                        .build());
        options.addOption(
                Option.builder("m")
                        .longOpt("max-batches")
                        .hasArg()
                        .argName("max-batches")
                        .desc(
                                "Limit each epoch to a fixed number of iterations to test the"
                                        + " training script")
                        .build());
        options.addOption(
                Option.builder("d")
                        .longOpt("model-dir")
                        .hasArg()
                        .argName("MODEL-DIR")
                        .desc("pre-trained model file directory")
                        .build());
        options.addOption(
                Option.builder("r")
                        .longOpt("criteria")
                        .hasArg()
                        .argName("CRITERIA")
                        .desc("The criteria used for the model.")
                        .build());
        return options;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int getEpoch() {
        return epoch;
    }

    public int getMaxGpus() {
        return maxGpus;
    }

    public boolean isSymbolic() {
        return isSymbolic;
    }

    public boolean isPreTrained() {
        return preTrained;
    }

    public String getModelDir() {
        return modelDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public long getLimit() {
        return limit;
    }

}