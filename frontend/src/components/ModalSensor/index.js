import React, { useEffect, useState } from "react";
import api from "../../services/api";
import { Button, Form, Modal, Row, Col } from "react-bootstrap";

const ModalSensor = ({ show, fecharModal }) => {
  const [status, setStatus] = useState("");
  const [id, setId] = useState("");
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");
  const [isLoading, setIsLoading] = useState("");

  const clear = () => {
    setStatus("");
    setId("");
    setLatitude("");
    setLongitude("");
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const newSensor = {
      id: id,
      type: "TrafficSensor",
      flow: { type: "Text", value: status },
      location: {
        type: "geo:json",
        value: {
          type: "Point",
          coordinates: [parseFloat(latitude), parseFloat(longitude)],
        },
      },
    };

    setIsLoading(true);
    try {
      await api.post("/sensors", newSensor);
      clear();
      fecharModal();
    } catch (error) {
      setIsLoading(false);
    }
    setIsLoading(false);
  };

  return (
    <Modal show={show} onHide={fecharModal}>
      <Modal.Header closeButton>
        <Modal.Title>Sensor Register</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Row>
          <Form.Group className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control
              type="text"
              placeholder="Sensor Name"
              value={id}
              onChange={(e) => setId(e.target.value)}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Status</Form.Label>
            <Form.Select onChange={(e) => setStatus(e.target.value)}>
              <option>Select...</option>
              <option value="busy">Busy</option>
              <option value="free">Free</option>
              <option value="moderate">Moderate</option>
            </Form.Select>
          </Form.Group>
          <Col sm={6}>
            <Form.Group className="mb-3">
              <Form.Label>Latitude</Form.Label>
              <Form.Control
                type="text"
                placeholder="Latitude"
                value={latitude}
                onChange={(e) => setLatitude(e.target.value)}
              />
            </Form.Group>
          </Col>
          <Col sm={6}>
            <Form.Group className="mb-3">
              <Form.Label>Longitude</Form.Label>
              <Form.Control
                type="text"
                placeholder="Longitude"
                value={longitude}
                onChange={(e) => setLongitude(e.target.value)}
              />
            </Form.Group>
          </Col>
        </Row>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={fecharModal}>
          Close
        </Button>
        <Button onClick={handleSubmit} disabled={isLoading}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalSensor;
