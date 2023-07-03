import Router from "next/router";
import Image from "next/image";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";

export function Menu() {
  return (
    <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
      <Container>
        <Navbar.Brand onClick={() => Router.push("/")} as="a">
          <Image
            src="/joe.png"
            alt={"joe-logo"}
            height={100}
            width={120}
          ></Image>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="responsive-navbar-nav" />

        <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="me-auto"></Nav>

          <Nav>
            <h4 style={{ color: "wheat" }}>Joe's Traffic</h4>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
