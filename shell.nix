# shell.nix
{
  pkgs ? import <nixpkgs> { },
}:

pkgs.mkShell {
  buildInputs = [
    pkgs.openjdk
    pkgs.maven
  ];
}
