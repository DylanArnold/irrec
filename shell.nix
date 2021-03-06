let

  # use a pinned version of nixpkgs for reproducability
  nixpkgs-version = "18.09";
  pkgs = import (builtins.fetchTarball {
    # Descriptive name to make the store path easier to identify
    name = "nixpkgs-${nixpkgs-version}";
    url = "https://github.com/nixos/nixpkgs/archive/${nixpkgs-version}.tar.gz";
    # Hash obtained using `nix-prefetch-url --unpack <url>`
    sha256 = "1ib96has10v5nr6bzf7v8kw7yzww8zanxgw2qi1ll1sbv6kj6zpd";
  }) {};

  sbt = with pkgs; symlinkJoin {
    name = "sbt-irrec";
    buildInputs = [ makeWrapper ];
    paths = [ pkgs.sbt ];
    postBuild = ''
      wrapProgram "$out/bin/sbt" \
      --add-flags "-mem 2048"
    '';
  };
in
  with pkgs;
  stdenv.mkDerivation {
    name = "irrec-dev-env";

    buildInputs = [
      sbt
      git # used by sbt-dynver
      graphviz # used for ScalaDoc diagrams
      nodejs # used by scala.js
      yarn # used by docusaurus for doc site
    ];
  }
